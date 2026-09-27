package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Catalog;
import com.example.capston2.Service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllCatalogs(){
        return ResponseEntity.status(200).body(catalogService.getAllCatalogs());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCatalog(@RequestBody @Valid Catalog catalog, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        try {
            catalogService.addCatalog(catalog);
            return ResponseEntity.status(200).body(new ApiResponse("Catalog was added"));
        } catch (Exception e){
            return ResponseEntity.status(500).body("AI Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCatalog(@PathVariable Integer id, @RequestBody @Valid Catalog catalog, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
            Boolean results = catalogService.updateCatalog(id, catalog);
            if (!results) {
                return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
            }
            return ResponseEntity.status(200).body(new ApiResponse("Catalog was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCatalog(@PathVariable Integer id){
        Boolean results = catalogService.deleteCatalog(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Catalog was deleted"));
    }
}