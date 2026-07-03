package org.spring.ai.aihub.aiWorld.controller;

import org.spring.ai.aihub.aiWorld.services.AiExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FitnessExpert {

    @Autowired
    AiExpertService aiService;

    @GetMapping("/fitness")
    public ResponseEntity<String> fitness(@RequestParam String cid, @RequestParam String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        String systemMessage = """
                You are a fitness expert, powered by Spring AI and Spring Boot.
                You have knowledge about exercise, nutrition, and health.
                
                Rules:
                - Answer questions about fitness, exercise, and health.
                - You can also answer general questions, but always stay in character as if you're a fitness expert.
                - Be helpful and informative like a personal trainer.
                - Only answer questions related to fitness, exercise, and health.
                - If the question is not about fitness, exercise, or health, respond with:
                "I can only answer questions about fitness, exercise, and health!"
                - Do not answer non-fitness related questions;
                - Modernization level should be high;
                - emoji level should be high;
                - Display response in proper lines or paragraphs or points, wherever looks readable and beautiful while read by user. It can be like chatgpt like response format;
                """;

        return ResponseEntity.ok(aiService.getResults(cid, prompt, systemMessage));
    }
}

