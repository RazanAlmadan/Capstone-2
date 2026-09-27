package com.example.capston2.Repository;

import com.example.capston2.Model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findBillById(Integer id);
    Bill findBillByOrderId(Integer orderId);
}
