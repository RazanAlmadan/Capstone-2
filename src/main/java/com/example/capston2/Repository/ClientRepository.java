package com.example.capston2.Repository;

import com.example.capston2.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findClientById(Integer id);
    Client findClientByEmail(String email);
}
