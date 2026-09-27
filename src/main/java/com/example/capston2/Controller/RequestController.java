package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Request;
import com.example.capston2.Service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllRequests(){
        return ResponseEntity.status(200).body(requestService.getAllRequests());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRequest(@RequestBody @Valid Request request, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Integer results = requestService.addRequest(request);
        if (results == 1){
            return ResponseEntity.status(400).body(new ApiResponse("Client ID was not found"));
        }
        if (results == 2){
            return ResponseEntity.status(400).body(new ApiResponse("Designer ID was not found"));
        }
        if (results == 3){
            return ResponseEntity.status(200).body(new ApiResponse("Can't create a request with Accepted or Rejected status"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Request was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Integer id, @RequestBody @Valid Request request, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Boolean results = requestService.updateRequest(id, request);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Request was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Integer id){
        Boolean results = requestService.deleteRequest(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Request was deleted"));
    }
}