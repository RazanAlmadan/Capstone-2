package com.example.capston2.Repository;

import com.example.capston2.Model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Rating findRatingById(Integer id);

    List<Rating> findRatingByDesignerId(Integer designerId);

}
