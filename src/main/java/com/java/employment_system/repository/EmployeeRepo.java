package com.java.employment_system.repository;

import com.java.employment_system.entity.Employee;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long> {
    Optional<Employee> findByFirstNameAndLastNameAndMiddleNameAndBirthday(String firstName, String lastName, String middleName, Date birthday);

    @Query("SELECT e FROM Employee e " +
            "WHERE (:firstName IS NULL OR UPPER(e.firstName) LIKE UPPER(CONCAT('%', :firstName, '%'))) " +
            "AND (:lastName IS NULL OR e.lastName LIKE CONCAT('%', :lastName, '%')) " +
            "AND (:position IS NULL OR e.position LIKE CONCAT('%', :position, '%'))")
    List<Employee> searchEmployees(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("position") String position);
    }


