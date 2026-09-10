package com.credscope.credscope.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column(nullable = false)
    private BigDecimal loanAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanPurpose purpose;

    @Column(nullable = false)
    private BigDecimal monthlyIncome;

    @Column(nullable = false)
    private BigDecimal existingEmi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    private Integer riskScore;

    @Lob
    private String aiRiskExplanation;

    @ManyToOne
    @JoinColumn(name = "decided_by")
    private User decidedBy;

    private LocalDateTime decidedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getApplicant() {
		return applicant;
	}

	public void setApplicant(User applicant) {
		this.applicant = applicant;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public LoanPurpose getPurpose() {
		return purpose;
	}

	public void setPurpose(LoanPurpose purpose) {
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

	public EmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public Integer getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(Integer riskScore) {
		this.riskScore = riskScore;
	}

	public String getAiRiskExplanation() {
		return aiRiskExplanation;
	}

	public void setAiRiskExplanation(String aiRiskExplanation) {
		this.aiRiskExplanation = aiRiskExplanation;
	}

	public User getDecidedBy() {
		return decidedBy;
	}

	public void setDecidedBy(User decidedBy) {
		this.decidedBy = decidedBy;
	}

	public LocalDateTime getDecidedAt() {
		return decidedAt;
	}

	public void setDecidedAt(LocalDateTime decidedAt) {
		this.decidedAt = decidedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LoanApplication(Long id, User applicant, BigDecimal loanAmount, LoanPurpose purpose,
			BigDecimal monthlyIncome, BigDecimal existingEmi, EmploymentType employmentType, ApplicationStatus status,
			Integer riskScore, String aiRiskExplanation, User decidedBy, LocalDateTime decidedAt,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.applicant = applicant;
		this.loanAmount = loanAmount;
		this.purpose = purpose;
		this.monthlyIncome = monthlyIncome;
		this.existingEmi = existingEmi;
		this.employmentType = employmentType;
		this.status = status;
		this.riskScore = riskScore;
		this.aiRiskExplanation = aiRiskExplanation;
		this.decidedBy = decidedBy;
		this.decidedAt = decidedAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public LoanApplication() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}