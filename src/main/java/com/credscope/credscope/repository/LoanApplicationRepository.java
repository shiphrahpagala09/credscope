package com.credscope.credscope.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.credscope.credscope.entity.LoanApplication;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long>{
	List<LoanApplication> findByApplicantEmail(String email);

}
