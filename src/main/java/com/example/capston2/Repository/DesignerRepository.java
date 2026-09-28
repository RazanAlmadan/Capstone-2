package com.example.capston2.Repository;

import com.example.capston2.Model.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignerRepository extends JpaRepository<Designer, Integer> {
    Designer findDesignerById(Integer id);
    Designer findDesignerByName(String name);
    List<Designer> findDesignerByVisualField(String visualField);

    @Query("SELECT d from Designer d ORDER BY averageRating DESC ")
    List<Designer> getBasedOnRating();

    @Query("SELECT d from Designer d Where d.visualField = ?1 ORDER BY averageRating DESC")
    List<Designer> getBasedOnRatingAndCategory(String category);

    @Query("SELECT d FROM Designer d WHERE " +
            "LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.visualField) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Designer> searchDesigners(@Param("keyword") String keyword);

    Designer findDesignerByEmail(String email);

}
