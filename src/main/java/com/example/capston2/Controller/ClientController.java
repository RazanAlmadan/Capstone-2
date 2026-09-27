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
    public ResponseEntity<?> addClient(@RequestBody @Valid Client client, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        clientService.addClient(client);
        return ResponseEntity.status(200).body(new ApiResponse("Client was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Integer id, @RequestBody @Valid Client client, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Boolean results = clientService.updateClient(id, client);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Client was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Integer id){
        Boolean results = clientService.deleteClient(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("client was deleted"));
    }

    @GetMapping("/search/by/name/{name}")
    public ResponseEntity<?> searchByName(@PathVariable String name){
        Designer designer = clientService.searchByName(name);
        if (designer==null){
            return ResponseEntity.status(400).body(new ApiResponse("Designer was not found"));
        }
        return ResponseEntity.status(200).body(designer);
    }

    @PostMapping("/accept/or/reject/proposal/{clientId}/{proposalId}/{status}")
    public ResponseEntity<?> acceptOrRejectProposal(@PathVariable Integer clientId, @PathVariable Integer proposalId, @PathVariable String status){
        Integer results = clientService.acceptOrRejectProposal(clientId,proposalId,status);
        if (results == -1){
            return ResponseEntity.status(400).body(new ApiResponse("proposal was not found"));
        }
        if (results == 1){
            return ResponseEntity.status(400).body(new ApiResponse("client was not found"));
        }
        if (results == 2){
            return ResponseEntity.status(400).body(new ApiResponse("Status can only be either Accepted or Rejected"));
        }
        if (results == 3){
            return ResponseEntity.status(400).body(new ApiResponse("proposal was already accepted"));
        }
        if (results == 4){
            return ResponseEntity.status(400).body(new ApiResponse("proposal was already rejected"));
        }
        if (results == 5){
            return ResponseEntity.status(200).body(new ApiResponse("proposal have been Rejected!"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("proposal have been Accepted! and a new bill was issued for down payment"));
    }

    @PostMapping("/send/message/{content}/{chatRoomId}")
    public ResponseEntity<?> sendAMessage(@PathVariable String content, @PathVariable Integer chatRoomId){
        Boolean results = clientService.sendAMessage(content, chatRoomId);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("chatRoom was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Message was sent"));
    }

    @PostMapping("/sent/message/with/attachment/{content}/{attachmentUrl}/{chatRoomId}")
    public ResponseEntity<?> sendAMessage(@PathVariable String content, @PathVariable String attachmentUrl, @PathVariable Integer chatRoomId){
        Boolean results = clientService.sendAMessage(content, attachmentUrl, chatRoomId);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("chatRoom was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Message was sent"));
    }

    @PutMapping("/pay/bill/{clientId}/{billId}")
    public ResponseEntity<?> payTheBill(@PathVariable Integer clientId, @PathVariable Integer billId){
        Integer results = clientService.payTheBill(clientId, billId);
        if (results == -1){
            return ResponseEntity.status(400).body(new ApiResponse("Bill was not found"));
        }
        if (results == -2){
            return ResponseEntity.status(400).body(new ApiResponse("Client was not found"));
        }
        if (results == 0){
            return ResponseEntity.status(400).body(new ApiResponse("This bill does not belong to the client with this ID"));
        }
        if (results == 1){
            return ResponseEntity.status(200).body(new ApiResponse("Down Payment was paid, designer can start working on the project"));
        }
        if (results == 2){
            return ResponseEntity.status(200).body(new ApiResponse("Full payment was paid"));
        }
        if (results == 3){
            return ResponseEntity.status(400).body(new ApiResponse("Can't pay the bill now"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Bill was paid"));
    }

    @PutMapping("/accept/reject/project/{clientId}/{projectId}/{status}")
    public ResponseEntity<?> acceptOrRejectProject(@PathVariable Integer clientId, @PathVariable Integer projectId, @PathVariable String status){
        Integer results = clientService.acceptOrRejectProject(clientId, projectId, status);
        if (results == -1){
            return ResponseEntity.status(400).body(new ApiResponse("Project was not found"));
        }
        if (results == 1){
            return ResponseEntity.status(400).body(new ApiResponse("order does not belong to client"));
        }
        if (results == 2){
            return ResponseEntity.status(400).body(new ApiResponse("status must be Accepted or Rejected only"));
        }
        if (results == 3){
            return ResponseEntity.status(400).body(new ApiResponse("project is not waiting for approval"));
        }
        if (results == 4){
            return ResponseEntity.status(200).body(new ApiResponse("Project was rejected"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Project was accepted you can now pay the rest of the bill"));
    }

    @GetMapping("/get/designers/by/category/{visualField}")
    public ResponseEntity<?> searchByCategory(@PathVariable String visualField){
        List<Designer> designers = clientService.searchByCategory(visualField);
        if (designers.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No designers for this category where found"));
        }
        return ResponseEntity.status(200).body(designers);
    }

    @GetMapping("/get/by/rating")
    public ResponseEntity<?> getDesignersOrderByRating(){
        List<Designer> designers = clientService.getDesignersOrderByRating();
        if (designers.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No designers where found"));
        }
        return ResponseEntity.status(200).body(designers);
    }

    @GetMapping("/get/by/rating/and/category/{visualField}")
    public ResponseEntity<?> getDesignersOrderByRatingAndCategory(@PathVariable String visualField){
        List<Designer> designers = clientService.getDesignersOrderByRatingAndCategory(visualField);
        if (designers.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No designers for this category where found"));
        }
        return ResponseEntity.status(200).body(designers);
    }

    @PostMapping("/send-request/{designerId}")
    public ResponseEntity<?> sendRequest(@PathVariable Integer designerId,
                                         @RequestParam String projectDetails,
                                         @RequestParam Integer clientId) {

        Integer result = clientService.sendRequest(clientId, designerId, projectDetails);

        if (result == 0) {
            return ResponseEntity.status(200).body("Request sent successfully");
        }
        return ResponseEntity.status(400).body("Failed to send request, error code: " + result);
    }


}
