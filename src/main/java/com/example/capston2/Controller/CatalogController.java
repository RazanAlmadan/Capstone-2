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
    public ResponseEntity<?> addCatalog(@RequestBody @Valid Catalog catalog){
            catalogService.addCatalog(catalog);
            return ResponseEntity.status(200).body(new ApiResponse("Catalog was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCatalog(@PathVariable Integer id, @RequestBody @Valid Catalog catalog){
            catalogService.updateCatalog(id, catalog);
            return ResponseEntity.status(200).body(new ApiResponse("Catalog was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCatalog(@PathVariable Integer id){
        catalogService.deleteCatalog(id);
        return ResponseEntity.status(200).body(new ApiResponse("Catalog was deleted"));
    }
}