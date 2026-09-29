package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Proposal;
import com.example.capston2.Service.ProposalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/proposal")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllProposals(){
        return ResponseEntity.status(200).body(proposalService.getAllProposals());
    }

    @PostMapping("/add/{designerId}/{clientId}")
    public ResponseEntity<?> addProposal(@PathVariable Integer designerId, @PathVariable Integer clientId, @RequestBody @Valid Proposal proposal){
        proposalService.addProposal(designerId, clientId, proposal);
        return ResponseEntity.status(200).body(new ApiResponse("Proposal was sent"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProposal(@PathVariable Integer id, @RequestBody @Valid Proposal proposal){
        proposalService.updateProposal(id, proposal);
        return ResponseEntity.status(200).body(new ApiResponse("Proposal was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProposal(@PathVariable Integer id){
        proposalService.deleteProposal(id);
        return ResponseEntity.status(200).body(new ApiResponse("Proposal was deleted"));
    }
}