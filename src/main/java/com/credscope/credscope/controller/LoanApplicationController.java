package com.credscope.credscope.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credscope.credscope.dto.LoanApplicationRequest;
import com.credscope.credscope.dto.LoanApplicationResponse;
import com.credscope.credscope.dto.StatusUpdateRequest;
import com.credscope.credscope.service.LoanApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/applications")
public class LoanApplicationController {
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@PreAuthorize("hasRole('APPLICANT')")
	@PostMapping
    public LoanApplicationResponse createApplication(@Valid @RequestBody LoanApplicationRequest request, Authentication authentication) {
        return loanApplicationService.createApplication(request,authentication.getName());
    }
	@PreAuthorize("hasAnyRole('UNDERWRITER','ADMIN')")
	@GetMapping
	public List<LoanApplicationResponse> getAllApplications() {
	    return loanApplicationService.getAllApplications();
	}

	@GetMapping("/{id}")
	public LoanApplicationResponse getApplicationById(@PathVariable Long id) {
	    return loanApplicationService.getApplicationById(id);
	}
	@PreAuthorize("hasRole('UNDERWRITER')")
	@PutMapping("/{id}/status")
	public LoanApplicationResponse updateStatus(@PathVariable Long id,
	        @RequestBody StatusUpdateRequest request,
	        Authentication authentication) {
	    return loanApplicationService.updateStatus(id, request.getStatus(), authentication.getName());
	}
	@PreAuthorize("hasRole('APPLICANT')")
	@GetMapping("/my")
	public List<LoanApplicationResponse> getMyApplications(
	        Authentication authentication) {

	    return loanApplicationService.getMyApplications(
	            authentication.getName()
	    );
	}

}
