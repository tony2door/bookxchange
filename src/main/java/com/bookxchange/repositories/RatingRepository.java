package com.bookxchange.repositories;

import com.bookxchange.model.Rating;
import com.bookxchange.model.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<RatingEntity, String> {


}
