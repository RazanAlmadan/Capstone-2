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
    public ResponseEntity<?> addDesigner(@RequestBody @Valid Designer designer){
        designerService.addDesigner(designer);
        return ResponseEntity.status(200).body(new ApiResponse("Designer was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDesigner(@PathVariable Integer id, @RequestBody @Valid Designer designer){
        designerService.updateDesigner(id, designer);
        return ResponseEntity.status(200).body(new ApiResponse("Designer was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDesigner(@PathVariable Integer id){
        designerService.deleteDesigner(id);
        return ResponseEntity.status(200).body(new ApiResponse("Designer was deleted"));
    }

    @GetMapping("/get/requests/{id}")
    public ResponseEntity<?> getRequests(@PathVariable Integer id){
        List<Request> requests = designerService.getRequests(id);
        return ResponseEntity.status(200).body(requests);
    }

    @PostMapping("approve/reject/request/{id}/{requestId}/{status}")
    public ResponseEntity<?> approveOrRejectRequest(@PathVariable Integer id, @PathVariable Integer requestId, @PathVariable String status){
        Object results = designerService.approveOrRejectRequest(id, requestId, status);
        return ResponseEntity.status(200).body(new ApiResponse(results.toString()));
    }

    @PostMapping("/send/message/{content}/{chatRoomId}")
    public ResponseEntity<?> sendMessage(@PathVariable String content, @PathVariable Integer chatRoomId){
        designerService.sendAMessage(content, chatRoomId);
        return ResponseEntity.status(200).body(new ApiResponse("Message was sent"));
    }

    @PostMapping("/sent/message/with/attachment/{content}/{attachmentUrl}/{chatRoomId}")
    public ResponseEntity<?> sendAMessage(@PathVariable String content, @PathVariable String attachmentUrl, @PathVariable Integer chatRoomId){
        designerService.sendAMessage(content, attachmentUrl, chatRoomId);
        return ResponseEntity.status(200).body(new ApiResponse("Message was sent"));
    }

    @PostMapping("/designer/send-proposal")
    public ResponseEntity<?> sendProposal(@RequestParam Integer designerId,
                                          @RequestParam Integer clientId,
                                          @RequestParam Integer chatRoomId,
                                          @RequestParam Double price,
                                          @RequestParam String details,
                                          @RequestParam String deadLine) {

        designerService.sendProposal(
                designerId,
                clientId,
                chatRoomId,
                price,
                details,
                LocalDate.parse(deadLine)
        );
        return ResponseEntity.status(200).body("Proposal sent successfully");
    }




}

