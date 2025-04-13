package com.example.paymentprocessing.data.repository;

import com.example.paymentprocessing.data.models.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {

}
