package com.example.capston2.Repository;

import com.example.capston2.Model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Integer> {
    Proposal findProposalById(Integer id);
}
