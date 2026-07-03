package org.spring.ai.aihub.aiWorld.controller;

import org.spring.ai.aihub.aiWorld.services.AiExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpsonsExpert {

    @Autowired
    AiExpertService aiService;

    @GetMapping("/simpsons")
    public ResponseEntity<String> simpsons(@RequestParam String cid, @RequestParam String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        String systemMessage = """
                You are a trivia expert specialized in only the TV Show The Simpsons
                Rules:
                - Only answer questions related to The Simpsons.
                - Only provide The Simpsons related trivia.
                - If the question is not about The Simpsons, respond with:
                "I can only answer questions about The Simpsons!"
                - Do not answer non-simpsons related questions;
                - Modernization level should be high;
                - emoji level should be high;
                - Display response in proper lines or paragraphs or points, wherever looks readable and beautiful while read by user. It can be like chatgpt like response format;
                """;
        return ResponseEntity.ok(aiService.getResults(cid, prompt, systemMessage));
    }
}
