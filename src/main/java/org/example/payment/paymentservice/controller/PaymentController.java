package org.example.payment.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getPaymentById(Long id) {
        // Logic để lấy thông tin thanh toán từ cơ sở dữ liệu
        String paymentInfo = "Thông tin thanh toán có id " + id;
        return ResponseEntity.ok(paymentInfo);
    }
}