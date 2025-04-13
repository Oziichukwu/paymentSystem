package com.example.paymentprocessing;

import com.example.paymentprocessing.data.dtos.RequestDtos.PaymentRequest;
import com.example.paymentprocessing.data.models.Parent;
import com.example.paymentprocessing.data.models.Payments;
import com.example.paymentprocessing.data.models.Student;
import com.example.paymentprocessing.data.repository.ParentRepository;
import com.example.paymentprocessing.data.repository.PaymentsRepository;
import com.example.paymentprocessing.data.repository.StudentRepository;
import com.example.paymentprocessing.exceptions.InsufficientBalanceException;
import com.example.paymentprocessing.exceptions.ParentNotFoundException;
import com.example.paymentprocessing.exceptions.StudentNotFoundException;
import com.example.paymentprocessing.service.PaymentLogService;
import com.example.paymentprocessing.service.PaymentService;
import com.example.paymentprocessing.service.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PaymentProcessingApplicationTests {

    @Test
    void contextLoads() {
    }

    @Mock
    private ParentRepository parentRepo;

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private PaymentsRepository paymentRepo;

    @Mock
    private PaymentLogService paymentLogService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Parent parentA;
    private Parent parentB;
    private Student student1;
    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        // Initialize test data
        parentA = new Parent();
        parentA.setId(1L);
        parentA.setBalance(BigDecimal.valueOf(100));

        parentB = new Parent();
        parentB.setId(2L);
        parentB.setBalance(BigDecimal.valueOf(50));

        student1 = new Student();
        student1.setId(1L);
        student1.setBalance(BigDecimal.valueOf(0));

        // Create payment request
        paymentRequest = new PaymentRequest();
        paymentRequest.setParentId(1L);
        paymentRequest.setStudentId(1L);
        paymentRequest.setPaymentAmount(BigDecimal.valueOf(40));

    }

    @Test
    void testProcessPayment_success() {
        // Mock the repositories
        when(parentRepo.findById(parentA.getId())).thenReturn(Optional.of(parentA));
        when(studentRepo.findById(student1.getId())).thenReturn(Optional.of(student1));
        when(parentRepo.findParentsByStudentId(student1.getId())).thenReturn(List.of(parentA, parentB));
        when(paymentRepo.save(Mockito.any(Payments.class))).thenReturn(new Payments());

        // Call the method under test
        assertDoesNotThrow(() -> paymentService.processPayment(paymentRequest));

        // Verify that the payment was logged
        verify(paymentRepo, times(1)).save(Mockito.any(Payments.class));
        verify(paymentLogService, times(0)).logFailedPayment(Mockito.any(Payments.class)); // No failed log
    }

    @Test
    void testProcessPayment_failedInsufficientBalance() {
        // Set insufficient balance for the parents
        parentA.setBalance(BigDecimal.valueOf(10));
        parentB.setBalance(BigDecimal.valueOf(10));

        // Mock the repositories
        when(parentRepo.findById(parentA.getId())).thenReturn(Optional.of(parentA));
        when(studentRepo.findById(student1.getId())).thenReturn(Optional.of(student1));
        when(parentRepo.findParentsByStudentId(student1.getId())).thenReturn(List.of(parentA, parentB));
        when(paymentRepo.save(Mockito.any(Payments.class))).thenReturn(new Payments());

        // Call the method under test and verify the exception
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> paymentService.processPayment(paymentRequest));
        assertEquals("Insufficient balance in one of the shared parents", exception.getMessage());

        // Verify that the payment was logged as failed
        verify(paymentLogService, times(1)).logFailedPayment(Mockito.any(Payments.class));
        verify(paymentRepo, times(0)).save(Mockito.any(Payments.class)); // No payment was saved
    }

    @Test
    void testProcessPayment_parentNotFound() {
        // Mock that parent is not found
        when(parentRepo.findById(parentA.getId())).thenReturn(Optional.empty());

        // Call the method under test and verify the exception
        ParentNotFoundException exception = assertThrows(ParentNotFoundException.class, () -> paymentService.processPayment(paymentRequest));
        assertEquals("Parent not found", exception.getMessage());

        // Verify that the payment was not logged
        verify(paymentLogService, times(0)).logFailedPayment(Mockito.any(Payments.class));
        verify(paymentRepo, times(0)).save(Mockito.any(Payments.class)); // No payment was saved
    }

    @Test
    void testProcessPayment_studentNotFound() {
        // Mock that the student is not found
        when(studentRepo.findById(student1.getId())).thenReturn(Optional.empty());

        // Mock that parent exists
        when(parentRepo.findById(parentA.getId())).thenReturn(Optional.of(parentA));

        // Call the method under test and verify the exception
        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> paymentService.processPayment(paymentRequest));
        assertEquals("Student not found", exception.getMessage());

        // Verify that the payment was not logged
        verify(paymentLogService, times(0)).logFailedPayment(Mockito.any(Payments.class));
        verify(paymentRepo, times(0)).save(Mockito.any(Payments.class)); // No payment was saved
    }
}
