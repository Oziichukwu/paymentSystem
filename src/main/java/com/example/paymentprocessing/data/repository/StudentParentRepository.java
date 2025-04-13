package com.example.paymentprocessing.data.repository;

import com.example.paymentprocessing.data.models.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentParentRepository extends JpaRepository<StudentParent, Long> {
    List<StudentParent> findByStudentId(Long studentId);
}