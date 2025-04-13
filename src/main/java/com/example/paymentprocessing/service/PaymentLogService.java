package com.example.paymentprocessing.service;


import com.example.paymentprocessing.data.models.Payments;
import com.example.paymentprocessing.data.repository.PaymentsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentLogService {

    @Autowired
    private PaymentsRepository paymentRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailedPayment(Payments payment) {
        paymentRepo.save(payment);
    }
}
