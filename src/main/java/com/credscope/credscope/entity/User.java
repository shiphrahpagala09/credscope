package com.credscope.credscope.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String name;

	    @Column(nullable = false, unique = true)
	    private String email;

	    @Column(nullable = false)
	    private String passwordHash;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private Role role;

	    @Column(unique = true)
	    private String cifNumber;

	    private String mobileNumber;

	    @Column(unique = true)
	    private String aadharNumber;

	    private String accountNumber;

	    private String occupation;

	    private LocalDateTime createdAt;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPasswordHash() {
			return passwordHash;
		}

		public void setPasswordHash(String passwordHash) {
			this.passwordHash = passwordHash;
		}

		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}

		public String getCifNumber() {
			return cifNumber;
		}

		public void setCifNumber(String cifNumber) {
			this.cifNumber = cifNumber;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}

		public String getAadharNumber() {
			return aadharNumber;
		}

		public void setAadharNumber(String aadharNumber) {
			this.aadharNumber = aadharNumber;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getOccupation() {
			return occupation;
		}

		public void setOccupation(String occupation) {
			this.occupation = occupation;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public User(Long id, String name, String email, String passwordHash, Role role, String cifNumber,
				String mobileNumber, String aadharNumber, String accountNumber, String occupation,
				LocalDateTime createdAt) {
			super();
			this.id = id;
			this.name = name;
			this.email = email;
			this.passwordHash = passwordHash;
			this.role = role;
			this.cifNumber = cifNumber;
			this.mobileNumber = mobileNumber;
			this.aadharNumber = aadharNumber;
			this.accountNumber = accountNumber;
			this.occupation = occupation;
			this.createdAt = createdAt;
		}

		public User() {
			super();
		}

		
		
	

}
