package com.credscope.credscope.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.credscope.credscope.entity.EmploymentType;
import com.credscope.credscope.entity.LoanApplication;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskAssessmentService {

    public Integer calculateRiskScore(LoanApplication application) {

        BigDecimal monthlyIncome = application.getMonthlyIncome();
        BigDecimal existingEmi = application.getExistingEmi();
        BigDecimal loanAmount = application.getLoanAmount();

        if (monthlyIncome == null
                || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        int score = 0;

        // 1. Existing EMI burden
        BigDecimal emiRatio = existingEmi
                .divide(monthlyIncome, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        if (emiRatio.compareTo(BigDecimal.valueOf(20)) <= 0) {
            score += 30;
        } else if (emiRatio.compareTo(BigDecimal.valueOf(35)) <= 0) {
            score += 22;
        } else if (emiRatio.compareTo(BigDecimal.valueOf(50)) <= 0) {
            score += 12;
        }

        // 2. Loan amount compared with annual income
        BigDecimal annualIncome = monthlyIncome.multiply(BigDecimal.valueOf(12));

        BigDecimal loanToAnnualIncome = loanAmount
                .divide(annualIncome, 4, RoundingMode.HALF_UP);

        if (loanToAnnualIncome.compareTo(BigDecimal.valueOf(2)) <= 0) {
            score += 25;
        } else if (loanToAnnualIncome.compareTo(BigDecimal.valueOf(4)) <= 0) {
            score += 18;
        } else if (loanToAnnualIncome.compareTo(BigDecimal.valueOf(6)) <= 0) {
            score += 10;
        } else {
            score += 3;
        }

        // 3. Employment stability
        EmploymentType employmentType = application.getEmploymentType();

        if (employmentType == EmploymentType.SALARIED) {
            score += 20;
        } else if (employmentType == EmploymentType.SELF_EMPLOYED) {
            score += 15;
        } else if (employmentType == EmploymentType.BUSINESS_OWNER) {
            score += 12;
        }

        return Math.min(score, 100);
    }

    public String getRiskCategory(Integer score) {

        if (score == null) {
            return "UNKNOWN";
        }

        if (score >= 70) {
            return "LOW";
        } else if (score >= 40) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }
    public List<String> getRiskFactors(LoanApplication application) {

        List<String> factors = new ArrayList<>();

        BigDecimal monthlyIncome = application.getMonthlyIncome();
        BigDecimal existingEmi = application.getExistingEmi();
        BigDecimal loanAmount = application.getLoanAmount();

        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            factors.add("Monthly income is missing or invalid");
            return factors;
        }

        BigDecimal emiRatio = existingEmi
                .divide(monthlyIncome, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        if (emiRatio.compareTo(BigDecimal.valueOf(20)) <= 0) {
            factors.add("Low existing EMI burden");
        } else if (emiRatio.compareTo(BigDecimal.valueOf(35)) <= 0) {
            factors.add("Moderate existing EMI burden");
        } else {
            factors.add("High existing EMI burden");
        }

        BigDecimal annualIncome = monthlyIncome.multiply(BigDecimal.valueOf(12));

        BigDecimal loanToAnnualIncome = loanAmount
                .divide(annualIncome, 4, RoundingMode.HALF_UP);

        if (loanToAnnualIncome.compareTo(BigDecimal.valueOf(2)) <= 0) {
            factors.add("Loan amount is relatively low compared with annual income");
        } else if (loanToAnnualIncome.compareTo(BigDecimal.valueOf(4)) <= 0) {
            factors.add("Loan amount is moderate compared with annual income");
        } else {
            factors.add("Loan amount is high compared with annual income");
        }

        EmploymentType employmentType = application.getEmploymentType();

        if (employmentType == EmploymentType.SALARIED) {
            factors.add("Stable salaried employment");
        } else if (employmentType == EmploymentType.SELF_EMPLOYED) {
            factors.add("Self-employed income requires additional assessment");
        } else if (employmentType == EmploymentType.BUSINESS_OWNER) {
            factors.add("Business income may require additional verification");
        }

        return factors;
    }
}