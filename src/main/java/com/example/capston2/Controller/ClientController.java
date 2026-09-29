package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Client;
import com.example.capston2.Model.Designer;
import com.example.capston2.Service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllClients(){
        return ResponseEntity.status(200).body(clientService.getAllClients());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addClient(@RequestBody @Valid Client client){
        clientService.addClient(client);
        return ResponseEntity.status(200).body(new ApiResponse("Client was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Integer id, @RequestBody @Valid Client client){
        clientService.updateClient(id, client);
        return ResponseEntity.status(200).body(new ApiResponse("Client was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Integer id){
        clientService.deleteClient(id);
        return ResponseEntity.status(400).body(new ApiResponse("client was deleted"));
    }

    @GetMapping("/search/by/name/{name}")
    public ResponseEntity<?> searchByName(@PathVariable String name){
        Designer designer = clientService.searchByName(name);
        return ResponseEntity.status(200).body(designer);
    }

    @PostMapping("/accept/or/reject/proposal/{clientId}/{proposalId}/{status}")
    public ResponseEntity<?> acceptOrRejectProposal(@PathVariable Integer clientId, @PathVariable Integer proposalId, @PathVariable String status){
        Object results = clientService.acceptOrRejectProposal(clientId,proposalId,status);
        return ResponseEntity.status(200).body(new ApiResponse(results.toString()));

    }

    @PostMapping("/send/message/{content}/{chatRoomId}")
    public ResponseEntity<?> sendAMessage(@PathVariable String content, @PathVariable Integer chatRoomId){
        clientService.sendAMessage(content, chatRoomId);
        return ResponseEntity.status(200).body(new ApiResponse("Message was sent"));
    }

    @PostMapping("/sent/message/with/attachment/{content}/{attachmentUrl}/{chatRoomId}")
    public ResponseEntity<?> sendAMessage(@PathVariable String content, @PathVariable String attachmentUrl, @PathVariable Integer chatRoomId){
        clientService.sendAMessage(content, attachmentUrl, chatRoomId);
        return ResponseEntity.status(200).body(new ApiResponse("Message was sent"));
    }

    @PutMapping("/pay/bill/{clientId}/{billId}")
    public ResponseEntity<?> payTheBill(@PathVariable Integer clientId, @PathVariable Integer billId){
        Object results = clientService.payTheBill(clientId, billId);
        return ResponseEntity.status(200).body(new ApiResponse(results.toString()));
    }

    @PutMapping("/accept/reject/project/{clientId}/{projectId}/{status}")
    public ResponseEntity<?> acceptOrRejectProject(@PathVariable Integer clientId, @PathVariable Integer projectId, @PathVariable String status){
        Object results = clientService.acceptOrRejectProject(clientId, projectId, status);
        return ResponseEntity.status(200).body(new ApiResponse(results.toString()));
    }

    @GetMapping("/get/designers/by/category/{visualField}")
    public ResponseEntity<?> searchByCategory(@PathVariable String visualField){
        List<Designer> designers = clientService.searchByCategory(visualField);
        return ResponseEntity.status(200).body(designers);
    }

    @GetMapping("/get/by/rating")
    public ResponseEntity<?> getDesignersOrderByRating(){
        List<Designer> designers = clientService.getDesignersOrderByRating();
        return ResponseEntity.status(200).body(designers);
    }

    @GetMapping("/get/by/rating/and/category/{visualField}")
    public ResponseEntity<?> getDesignersOrderByRatingAndCategory(@PathVariable String visualField){
        List<Designer> designers = clientService.getDesignersOrderByRatingAndCategory(visualField);
        return ResponseEntity.status(200).body(designers);
    }

    @PostMapping("/send-request/{designerId}")
    public ResponseEntity<?> sendRequest(@PathVariable Integer designerId, @RequestParam String projectDetails, @RequestParam Integer clientId) {
        clientService.sendRequest(clientId, designerId, projectDetails);
        return ResponseEntity.status(200).body("Request sent successfully");
    }


}
