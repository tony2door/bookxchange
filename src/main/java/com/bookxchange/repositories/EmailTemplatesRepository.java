package com.bookxchange.repositories;

import com.bookxchange.model.EmailTemplatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplatesRepository extends JpaRepository<EmailTemplatesEntity,Integer> {
}
