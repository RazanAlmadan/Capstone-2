package com.example.capston2.Service;

import com.example.capston2.Model.Bill;
import com.example.capston2.Repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;

    public List<Bill> getAllBills(){
        return billRepository.findAll();
    }

    public void addBill(Bill bill){
        billRepository.save(bill);
    }

    public Boolean updateBill(Integer id, Bill bill){
        Bill oldBill = billRepository.findBillById(id);
        if (oldBill == null){
            return false;
        }
        oldBill.setOrderId(bill.getOrderId());
        oldBill.setDownPayment(bill.getDownPayment());
        oldBill.setDownPayment(bill.getDownPayment());
        oldBill.setFullPayment(bill.getFullPayment());
        oldBill.setStatues(bill.getStatues());
        billRepository.save(oldBill);
        return true;
    }

    public Boolean deleteBill(Integer id){
        Bill oldBill = billRepository.findBillById(id);
        if (oldBill == null){
            return false;
        }
        billRepository.delete(oldBill);
        return true;
    }
}