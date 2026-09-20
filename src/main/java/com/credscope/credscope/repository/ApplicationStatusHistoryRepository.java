package com.credscope.credscope.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.credscope.credscope.entity.ApplicationStatusHistory;

public interface ApplicationStatusHistoryRepository
        extends JpaRepository<ApplicationStatusHistory, Long> {

    List<ApplicationStatusHistory> findByLoanApplicationIdOrderByChangedAtAsc(
            Long loanApplicationId);
}