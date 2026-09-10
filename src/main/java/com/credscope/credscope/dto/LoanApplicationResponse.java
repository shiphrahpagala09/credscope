package com.credscope.credscope.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LoanApplicationResponse {
	  private Long id;
	    private String applicantName;
	    private BigDecimal loanAmount;
	    private String purpose;
	    private String status;
	    private Integer riskScore;
	    private LocalDateTime createdAt;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getApplicantName() {
			return applicantName;
		}
		public void setApplicantName(String applicantName) {
			this.applicantName = applicantName;
		}
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
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Integer getRiskScore() {
			return riskScore;
		}
		public void setRiskScore(Integer riskScore) {
			this.riskScore = riskScore;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	    

}
