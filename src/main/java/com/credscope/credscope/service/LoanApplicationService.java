package com.credscope.credscope.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credscope.credscope.dto.LoanApplicationRequest;
import com.credscope.credscope.dto.LoanApplicationResponse;
import com.credscope.credscope.entity.ApplicationStatus;
import com.credscope.credscope.entity.EmploymentType;
import com.credscope.credscope.entity.LoanApplication;
import com.credscope.credscope.entity.LoanPurpose;
import com.credscope.credscope.entity.User;
import com.credscope.credscope.repository.LoanApplicationRepository;
import com.credscope.credscope.repository.UserRepository;

@Service
public class LoanApplicationService {
	@Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private UserRepository userRepository;
     
    public LoanApplicationResponse  createApplication(LoanApplicationRequest request,String applicantEmail) {
    	User applicant = userRepository.findByEmail(applicantEmail)
    	        .orElseThrow(() -> new RuntimeException("Applicant not found"));    	
    	LoanApplication application = new LoanApplication();
    	application.setApplicant(applicant);
        application.setLoanAmount(request.getLoanAmount());
        application.setPurpose(LoanPurpose.valueOf(request.getPurpose()));
        application.setMonthlyIncome(request.getMonthlyIncome());
        application.setExistingEmi(request.getExistingEmi());
        application.setEmploymentType(EmploymentType.valueOf(request.getEmploymentType()));
        application.setStatus(ApplicationStatus.SUBMITTED);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());
        
        LoanApplication saved =loanApplicationRepository.save(application);
        
//        LoanApplicationResponse response = new LoanApplicationResponse();SSS
//        response.setId(saved.getId());
//        response.setApplicantName(saved.getApplicant().getName());
//        response.setLoanAmount(saved.getLoanAmount());
//        response.setPurpose(saved.getPurpose().name());
//        response.setStatus(saved.getStatus().name());
//        response.setRiskScore(saved.getRiskScore());
//        response.setCreatedAt(saved.getCreatedAt());
       
        return toResponse(saved);
    }
    public List<LoanApplicationResponse> getAllApplications() {
        return loanApplicationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public LoanApplicationResponse getApplicationById(Long id) {
        LoanApplication application = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        return toResponse(application);
    }

    private LoanApplicationResponse toResponse(LoanApplication saved) {
        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setId(saved.getId());
        response.setApplicantName(saved.getApplicant().getName());
        response.setLoanAmount(saved.getLoanAmount());
        response.setPurpose(saved.getPurpose().name());
        response.setStatus(saved.getStatus().name());
        response.setRiskScore(saved.getRiskScore());
        response.setCreatedAt(saved.getCreatedAt());
        return response;
    }
    public LoanApplicationResponse updateStatus(Long id, String newStatus, String underwriterEmail) {
        LoanApplication application = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        ApplicationStatus status = ApplicationStatus.valueOf(newStatus);
        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());

        if (status == ApplicationStatus.APPROVED || status == ApplicationStatus.REJECTED) {
            User underwriter = userRepository.findByEmail(underwriterEmail)
                    .orElseThrow(() -> new RuntimeException("Underwriter not found"));
            application.setDecidedBy(underwriter);
            application.setDecidedAt(LocalDateTime.now());
        }

        return toResponse(loanApplicationRepository.save(application));
    }

}
