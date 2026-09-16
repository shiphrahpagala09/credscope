package com.credscope.credscope.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class LoanApplicationRequest {
//	@NotNull
//	private Long applicantId; // temporary — will be replaced by logged-in user once auth exists
	@NotNull
	@Positive
	private BigDecimal loanAmount;
	@NotBlank
    private String purpose;
	@NotNull
	@PositiveOrZero
    private BigDecimal monthlyIncome;
	@NotNull 
	@PositiveOrZero
    private BigDecimal existingEmi;
	@NotNull
    private String employmentType;
//	public Long getApplicantId() {
//		return applicantId;
//	}
//	public void setApplicantId(Long applicantId) {
//		this.applicantId = applicantId;
//	}
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public BigDecimal getExistingEmi() {
		return existingEmi;
	}
	public void setExistingEmi(BigDecimal existingEmi) {
		this.existingEmi = existingEmi;
	}
	public String getEmploymentType() {
		return employmentType;
	}
	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}
    

}
