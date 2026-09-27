package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Designer;
import com.example.capston2.Model.Proposal;
import com.example.capston2.Model.Request;
import com.example.capston2.Service.DesignerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/designer")
@RequiredArgsConstructor
public class DesignerController {

    private final DesignerService designerService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllDesigners(){
        return ResponseEntity.status(200).body(designerService.getAllDesigners());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDesigner(@RequestBody @Valid Designer designer, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        designerService.addDesigner(designer);
        return ResponseEntity.status(200).body(new ApiResponse("Designer was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDesigner(@PathVariable Integer id, @RequestBody @Valid Designer designer, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Boolean results = designerService.updateDesigner(id, designer);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Designer was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDesigner(@PathVariable Integer id){
        Boolean results = designerService.deleteDesigner(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Designer was deleted"));
    }

    @GetMapping("/get/requests/{id}")
    public ResponseEntity<?> getRequests(@PathVariable Integer id){
        List<Request> requests = designerService.getRequests(id);
        if (requests.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("There Is not request available"));
        }
        return ResponseEntity.status(200).body(requests);
    }

    @PostMapping("approve/reject/request/{id}/{requestId}/{status}")
    public ResponseEntity<?> approveOrRejectRequest(@PathVariable Integer id, @PathVariable Integer requestId, @PathVariable String status){
        Integer results = designerService.approveOrRejectRequest(id, requestId, status);
        if (results == -1){
            return ResponseEntity.status(400).body(new ApiResponse("Request was not found"));
        }
        if (results == 1){
            return ResponseEntity.status(400).body(new ApiResponse("This request does not belong to the designer with this id"));
        }
        if (results == 2){
            return ResponseEntity.status(400).body(new ApiResponse("status must be either Accepted or Rejected"));
        }
        if (results == 3){
            return ResponseEntity.status(400).body(new ApiResponse("Request was already accepted"));
        }
        if (results == 4){
            return ResponseEntity.status(400).body(new ApiResponse("Request was already rejected"));
        }
        if (results == 5){
            return ResponseEntity.status(200).body(new ApiResponse("Request have been Rejected!"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Request have been Accepted!"));
    }

    @PostMapping("/send/message/{content}/{chatRoomId}")
    public ResponseEntity<?> sendMessage(@PathVariable String content, @PathVariable Integer chatRoomId){
        Boolean results = designerService.sendAMessage(content, chatRoomId);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("chatRoom was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Message was sent"));
    }

    @PostMapping("/sent/message/with/attachment/{content}/{attachmentUrl}/{chatRoomId}")
    public ResponseEntity<?> sendAMessage(@PathVariable String content, @PathVariable String attachmentUrl, @PathVariable Integer chatRoomId){
        Boolean results = designerService.sendAMessage(content, attachmentUrl, chatRoomId);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("chatRoom was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Message was sent"));
    }

    @PostMapping("/designer/send-proposal")
    public ResponseEntity<?> sendProposal(@RequestParam Integer designerId,
                                          @RequestParam Integer clientId,
                                          @RequestParam Integer chatRoomId,
                                          @RequestParam Double price,
                                          @RequestParam String details,
                                          @RequestParam String deadLine) {

        Integer result = designerService.sendProposal(
                designerId,
                clientId,
                chatRoomId,
                price,
                details,
                LocalDate.parse(deadLine)
        );

        if (result == 0) {
            return ResponseEntity.status(200).body("Proposal sent successfully");
        }
        return ResponseEntity.status(400).body("Failed to send proposal, error code: " + result);
    }




}

