package com.credscope.credscope.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.credscope.credscope.entity.RiskAssessment;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {

    Optional<RiskAssessment> findByLoanApplicationId(Long loanApplicationId);
}