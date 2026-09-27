package com.example.capston2.AI;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Designer;
import com.example.capston2.Service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final ProposalService proposalService;

    @PostMapping("/find-designers-by-style")
    public ResponseEntity<?> findDesignersByStyle(@RequestParam String imageUrl) {
        try {
            List<Designer> designers = aiService.findDesignersByStyle(imageUrl);
            return ResponseEntity.status(200).body(designers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("AI Error: " + e.getMessage());
        }
    }

    @PostMapping("/generate-proposal/{chatRoomId}")
    public ResponseEntity<?> generateProposal(@PathVariable Integer chatRoomId) {
        String proposal = aiService.generateProposal(chatRoomId);
        return ResponseEntity.status(200).body(proposal);
    }

    @PostMapping("/proposal/accept/{chatRoomId}")
    public ResponseEntity<?> acceptAIProposal(@PathVariable Integer chatRoomId) {

        String aiText = aiService.generateProposal(chatRoomId);

        Double price = aiService.extractPrice(aiText);
        LocalDate deadline = aiService.extractDeadline(aiText);
        String details = aiService.cleanDetails(aiText);

        proposalService.addAIProposal(chatRoomId, price, deadline, details);



        return ResponseEntity.ok(new ApiResponse("AI proposal sent to client"));
    }

}
