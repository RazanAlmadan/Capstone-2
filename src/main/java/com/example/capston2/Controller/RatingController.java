package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Rating;
import com.example.capston2.Service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllRatings(){
        return ResponseEntity.status(200).body(ratingService.getAllRatings());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRating(@RequestBody @Valid Rating rating, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Integer results = ratingService.addRating(rating);
        if (results == -1){
            return ResponseEntity.status(400).body(new ApiResponse("client was not found"));
        }
        if (results == -2){
            return ResponseEntity.status(400).body(new ApiResponse("designer was not found"));
        }
        if (results == -3){
            return ResponseEntity.status(400).body(new ApiResponse("Order was not found"));
        }
        if (results == 1){
            return ResponseEntity.status(400).body(new ApiResponse("Order status is not Done"));
        }
        if (results == 2){
            return ResponseEntity.status(200).body(new ApiResponse("Order does not belong to client or designer"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Rating was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRating(@PathVariable Integer id, @RequestBody @Valid Rating rating, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Boolean results = ratingService.updateRating(id, rating);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Rating was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Integer id){
        Boolean results = ratingService.deleteRating(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Rating was deleted"));
    }
}