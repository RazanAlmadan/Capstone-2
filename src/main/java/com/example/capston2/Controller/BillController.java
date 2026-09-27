package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Bill;
import com.example.capston2.Service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllBills(){
        return ResponseEntity.status(200).body(billService.getAllBills());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBill(@RequestBody @Valid Bill bill, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        billService.addBill(bill);
        return ResponseEntity.status(200).body(new ApiResponse("Bill was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBill(@PathVariable Integer id, @RequestBody @Valid Bill bill, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Boolean results = billService.updateBill(id, bill);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Bill was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Integer id){
        Boolean results = billService.deleteBill(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Bill was deleted"));
    }
}