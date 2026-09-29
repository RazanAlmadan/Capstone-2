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
    public ResponseEntity<?> addRequest(@RequestBody @Valid Request request){
        requestService.addRequest(request);
        return ResponseEntity.status(200).body(new ApiResponse("Request was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Integer id, @RequestBody @Valid Request request){
        requestService.updateRequest(id, request);
        return ResponseEntity.status(200).body(new ApiResponse("Request was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Integer id){
        requestService.deleteRequest(id);
        return ResponseEntity.status(200).body(new ApiResponse("Request was deleted"));
    }
}