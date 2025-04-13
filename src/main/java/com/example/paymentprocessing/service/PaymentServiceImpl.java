package com.example.paymentprocessing.service;


import com.example.paymentprocessing.data.dtos.RequestDtos.PaymentRequest;
import com.example.paymentprocessing.data.models.Parent;
import com.example.paymentprocessing.data.models.Payments;
import com.example.paymentprocessing.data.models.Student;
import com.example.paymentprocessing.data.models.StudentParent;
import com.example.paymentprocessing.data.repository.ParentRepository;
import com.example.paymentprocessing.data.repository.PaymentsRepository;
import com.example.paymentprocessing.data.repository.StudentRepository;
import com.example.paymentprocessing.exceptions.InsufficientBalanceException;
import com.example.paymentprocessing.exceptions.ParentNotFoundException;
import com.example.paymentprocessing.exceptions.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService{
    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;
    private final PaymentsRepository paymentRepo;

    private final PaymentLogService paymentLogService;

    @Autowired
    public PaymentServiceImpl(ParentRepository parentRepo, StudentRepository studentRepo, PaymentsRepository paymentRepo, PaymentLogService paymentLogService) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
        this.paymentRepo = paymentRepo;
        this.paymentLogService = paymentLogService;
    }


    @Override
    @Transactional
    public void processPayment(PaymentRequest paymentRequest) {
        /**
         *.This method process payment for unique and shared parent
         */
        Parent payingParent = parentRepo.findById(paymentRequest.getParentId())
                .orElseThrow(() -> new ParentNotFoundException("Parent not found"));

        Student student = studentRepo.findById(paymentRequest.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        BigDecimal dynamicRate = BigDecimal.valueOf(0.05); // 5% fee
        BigDecimal adjustedAmount = paymentRequest.getPaymentAmount().multiply(BigDecimal.ONE.add(dynamicRate));

        /**
         *. Set the payment / ledger record
         */
        Payments payment = new Payments();
        payment.setParentId(payingParent.getId());
        payment.setStudentId(student.getId());
        payment.setAmount(paymentRequest.getPaymentAmount());
        payment.setAdjustedAmount(adjustedAmount);
        payment.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        /**
         *.Get the parents linked or associated to a student
         */
        List<Parent> linkedParents = parentRepo.findParentsByStudentId(student.getId());

        if (linkedParents.size() > 1) {
            BigDecimal half = adjustedAmount.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
            for (Parent parent : linkedParents) {
                if (parent.getBalance().compareTo(half) < 0) {
                    payment.setStatus("FAILED - Insufficient balance in one of the shared parents");
                    paymentLogService.logFailedPayment(payment);
                    throw new InsufficientBalanceException("Insufficient balance in one of the shared parents");
                }
            }
            for (Parent parent : linkedParents) {
                parent.setBalance(parent.getBalance().subtract(half));
                parentRepo.save(parent);
            }
        } else {
            if (payingParent.getBalance().compareTo(adjustedAmount) < 0) {
                payment.setStatus("FAILED - Insufficient balance");
                paymentLogService.logFailedPayment(payment);
                throw new InsufficientBalanceException("Insufficient balance on the parents account");
            }
            payingParent.setBalance(payingParent.getBalance().subtract(adjustedAmount));
            parentRepo.save(payingParent);
        }

        student.setBalance(student.getBalance().add(adjustedAmount));
        studentRepo.save(student);

        payment.setStatus("SUCCESS");
        paymentRepo.save(payment);
    }

}
