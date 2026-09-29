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
    public ResponseEntity<?> addRating(@RequestBody @Valid Rating rating){
        ratingService.addRating(rating);
        return ResponseEntity.status(200).body(new ApiResponse("Rating was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRating(@PathVariable Integer id, @RequestBody @Valid Rating rating){
        ratingService.updateRating(id, rating);
        return ResponseEntity.status(200).body(new ApiResponse("Rating was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Integer id){
        ratingService.deleteRating(id);
        return ResponseEntity.status(200).body(new ApiResponse("Rating was deleted"));
    }
}