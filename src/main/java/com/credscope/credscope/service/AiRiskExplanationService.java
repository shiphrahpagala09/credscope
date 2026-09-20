package com.credscope.credscope.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.credscope.credscope.entity.LoanApplication;

@Service
public class AiRiskExplanationService {

    private final ChatClient chatClient;

    public AiRiskExplanationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateExplanation(
            LoanApplication application,
            Integer riskScore,
            String riskCategory,
            List<String> riskFactors) {

        String prompt = """
                You are an AI assistant for CredScope, an AI-assisted loan
                underwriting platform.

                Analyze the following already-calculated loan risk assessment.

                IMPORTANT RULES:
                1. Do NOT calculate or change the risk score.
                2. Do NOT make the final approve/reject decision.
                3. Do NOT invent applicant information.
                4. Use only the information provided below.
                5. Explain the factors that influenced the assessment.
                6. Keep the explanation professional and concise.
                7. State that the result is an AI-assisted explanation
                   requiring human underwriter review.

                Loan Details:
                Loan Amount: %s
                Monthly Income: %s
                Existing EMI: %s
                Employment Type: %s
                Loan Purpose: %s

                Existing Risk Assessment:
                Risk Score: %s
                Risk Category: %s

                Risk Factors:
                %s

                Generate:
                - A short overall assessment
                - Key positive factors
                - Key concerns
                - A final note stating that a human underwriter
                  must make the final decision

                Do not assign a new score.
                """.formatted(
                application.getLoanAmount(),
                application.getMonthlyIncome(),
                application.getExistingEmi(),
                application.getEmploymentType(),
                application.getPurpose(),
                riskScore,
                riskCategory,
                String.join(", ", riskFactors)
        );

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}