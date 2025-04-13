package com.example.paymentprocessing.data.repository;

import com.example.paymentprocessing.data.models.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    @Query(value = """
    SELECT p.* FROM parent p
    JOIN student_parent sp ON p.id = sp.parent_id
    WHERE sp.student_id = :studentId
    """, nativeQuery = true)
    List<Parent> findParentsByStudentId(@Param("studentId") Long studentId);
}
