package com.example.capston2.Repository;

import com.example.capston2.Model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    Request findRequestById(Integer id);
    List<Request> findRequestByDesignerId(Integer designerId);
}
