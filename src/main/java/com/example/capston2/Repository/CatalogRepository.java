package com.example.capston2.Repository;

import com.example.capston2.Model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {
    Catalog findCatalogById(Integer id);
    List<Catalog> findByDesignerId(Integer designerId);
}
