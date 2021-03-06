package com.bookxchange.service;

import com.bookxchange.enums.BookStatus;
import com.bookxchange.enums.EmailTemplateType;
import com.bookxchange.exception.NotificationException;
import com.bookxchange.model.BookMarketEntity;
import com.bookxchange.model.EmailEntity;
import com.bookxchange.model.NotificationEntity;
import com.bookxchange.pojo.NotificationHelper;
import com.bookxchange.repository.BookMarketRepository;
import com.bookxchange.repository.EmailsRepository;
import com.bookxchange.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Log LOG = LogFactory.getLog(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final BookMarketRepository bmr;
    private final EmailService emailService;
    private final EmailsRepository emailsRepository;

    @Transactional
    public void checkForNotifications() {
        try {
            List<NotificationHelper> emailToNotify = notificationRepository.getEmailToNotify();
            if (!emailToNotify.isEmpty()) {
                emailToNotify.stream().filter(customer -> customer.getTemplate_Name().equals(EmailTemplateType.AVAILABILITY)).forEach(this::sendMail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Notification fatal error.");
        }
    }

    public NotificationEntity addNotification(String marketBookId, String memberId) {

        boolean isDuplicate = notificationRepository.existsNotificationsEntitiesByMarketBookUuidAndMemberUuid(marketBookId, memberId);

        Optional<BookMarketEntity> bookMarket = bmr.findByBookMarketUuid(marketBookId);
        NotificationEntity notification = new NotificationEntity();
        if (bookMarket.isPresent()) {
            BookMarketEntity bookMarketEntity = bookMarket.get();
            String status = bookMarketEntity.getBookStatus();
            if (!isDuplicate && !isAvailableOrSold(status)) {
                notification.setMemberUuid(memberId);
                notification.setMarketBookUuid(marketBookId);
                notification.setTemplateType(1);
                notification.setSent((byte) 0);
            } else if (isAvailableOrSold(status)) {
                String format = String.format("Book is already '%s'", status);
                LOG.debug("format : " + format);
                throw new NotificationException(format);
            } else if (isDuplicate) {
                throw new NotificationException("Duplicate Notification");
            }
        } else {
            throw new NotificationException("Empty BookMarket");
        }
        return notificationRepository.save(notification);
    }

    public boolean isAvailableOrSold(String status) {
        return status.equals(BookStatus.AVAILABLE.toString()) || status.equals(BookStatus.SOLD.toString());
    }


    private void sendMail(NotificationHelper userToBeNotifiedInfo) {
        EmailEntity emailsEntity = new EmailEntity();
        try {
            String body = String.format(userToBeNotifiedInfo.getContent_Body(), userToBeNotifiedInfo.getUsername(), userToBeNotifiedInfo.getTitle());
            emailService.sendMail(userToBeNotifiedInfo.getEmail_Address(), userToBeNotifiedInfo.getSubject(), body, userToBeNotifiedInfo.getMember_User_Id());
            notificationRepository.updateToSent(userToBeNotifiedInfo.getNotid());
            emailsEntity.setContent(body);
            emailsEntity.setSentDate(Date.valueOf(LocalDate.now()));
            emailsEntity.setMemberId(userToBeNotifiedInfo.getMember_User_Id());
            emailsEntity.setStatus("SENT");
            LOG.debug("emailEntity created : " + emailsEntity);
        } catch (Exception e) {
            emailsEntity.setStatus("ERROR");
            LOG.info("Error in sending e-mail.");
        }
    }
}
