package com.example.capston2.Repository;

import com.example.capston2.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Project findProjectById(Integer Id);
    Project findProjectByOrderId(Integer orderId);
}
