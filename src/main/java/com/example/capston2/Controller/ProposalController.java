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
    public ResponseEntity<?> addProposal(@PathVariable Integer designerId, @PathVariable Integer clientId, @RequestBody @Valid Proposal proposal, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Integer results = proposalService.addProposal(designerId, clientId, proposal);
        if (results == 1){
            return ResponseEntity.status(400).body(new ApiResponse("Client was not found"));
        }
        if (results == 2){
            return ResponseEntity.status(400).body(new ApiResponse("Designer was not found"));
        }
        if (results == 3){
            return ResponseEntity.status(400).body(new ApiResponse("chatRoom was not found"));
        }
        if (results == 4){
            return ResponseEntity.status(400).body(new ApiResponse("Client does not belong to the chatRoom"));
        }
        if (results == 5){
            return ResponseEntity.status(400).body(new ApiResponse("designer does not belong to the chatRoom"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Proposal was sent"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProposal(@PathVariable Integer id, @RequestBody @Valid Proposal proposal, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Integer results = proposalService.updateProposal(id, proposal);
        if (results == -1){
            return ResponseEntity.status(400).body(new ApiResponse("Proposal was not found"));
        }
        if (results == 1){
            return ResponseEntity.status(400).body(new ApiResponse("Proposal was accepted you can't update it"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Proposal was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProposal(@PathVariable Integer id){
        Boolean results = proposalService.deleteProposal(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Proposal was deleted"));
    }
}