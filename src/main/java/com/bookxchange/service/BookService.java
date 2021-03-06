package com.bookxchange.service;

import com.bookxchange.model.AuthorEntity;
import com.bookxchange.model.BookEntity;
import com.bookxchange.pojo.RetrievedBook;
import com.bookxchange.repository.AuthorsRepository;
import com.bookxchange.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class BookService {


    private final BooksRepository bookRepository;
    private final IsbnService workingIsbnService;
    private final AuthorsService workingAuthorsService;
    private final AuthorsRepository workingAuthorsRepository;

    @Autowired
    public BookService(BooksRepository bookRepository, IsbnService workingIsbnService, AuthorsService workingAuthorsService, AuthorsRepository workingAuthorsRepository) {
        this.bookRepository = bookRepository;
        this.workingIsbnService = workingIsbnService;
        this.workingAuthorsService = workingAuthorsService;
        this.workingAuthorsRepository = workingAuthorsRepository;
    }

    public BookEntity retrieveBookFromDB(String providedIsbn) {
        return bookRepository.getByIsbn(providedIsbn);
    }

    public List<BookEntity> userRetrievesBookList() {
        return bookRepository.findAll();
    }

    public void addNewBookToDB(BookEntity providedBook) {

        for (int i = 0; i < providedBook.getAuthors().size(); i++) {

            AuthorEntity tempAuthor;
            tempAuthor = workingAuthorsService.attemptAuthorDatabaseRetrival(
                    providedBook.getAuthors().get(i));
            if (tempAuthor.getName() != null) {
                providedBook.getAuthors().get(i).setAuthorsUuid(tempAuthor.getAuthorsUuid());
                providedBook.getAuthors().get(i).setId(tempAuthor.getId());
            } else {
                providedBook.getAuthors().get(i).setAuthorsUuid(UUID.randomUUID().toString());
                workingAuthorsRepository.save(providedBook.getAuthors().get(i));
            }

        }

        bookRepository.save(providedBook);
    }

    @Transactional
    public void updateQuantityAtAdding(String providedIsbn) {
        bookRepository.updateQuantityAdd(providedIsbn);
    }

    @Transactional
    public void downgradeQuantityForTransaction(String providedIsbn) {
        bookRepository.downgradeQuantityForTransaction(providedIsbn);
    }


    public BookEntity getBookByIsbn(String isbn) {
        return bookRepository.getBookEntityByIsbn(isbn);
    }

    @Transactional
    public RetrievedBook checkDbOrAttemptToPopulateFromIsbn(String providedIsbn) {

        RetrievedBook retrievedBookToReturn = new RetrievedBook(providedIsbn);
        retrievedBookToReturn.setRetrievedInfo(false);
        retrievedBookToReturn.setIsbn(providedIsbn);

        BookEntity bookToReturn = getBookByIsbn(providedIsbn);

        if (bookToReturn == null) {
            bookToReturn = workingIsbnService.hitIsbnBookRequest(providedIsbn);
            if (bookToReturn != null) {
                retrievedBookToReturn.setRetrievedInfo(true);
                retrievedBookToReturn.setRetrievedBook(bookToReturn);
                addNewBookToDB(bookToReturn);
            }
        } else {
            retrievedBookToReturn.setRetrievedBook(bookToReturn);
            retrievedBookToReturn.setRetrievedInfo(true);
        }

        return retrievedBookToReturn;
    }

}