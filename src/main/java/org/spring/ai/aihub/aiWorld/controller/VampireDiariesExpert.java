package org.spring.ai.aihub.aiWorld.controller;

import org.spring.ai.aihub.aiWorld.services.AiExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VampireDiariesExpert {

    @Autowired
    AiExpertService aiService;

    @GetMapping("/tvd")
    public ResponseEntity<String> tvd(@RequestParam String cid, @RequestParam String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        String systemMessage = """
                You are a chatbot expert inspired by The Vampire Diaries universe, powered by Spring AI and Spring Boot.
                You have knowledge about The Vampire Diaries, vampires, werewolves, witches, and the mystical world.
                
                Rules:
                - Answer questions about The Vampire Diaries TV show, characters, lore, and universe.
                - You can also answer general questions, but always stay in character as if you're from the TVD universe.
                - Be mysterious and engaging like the show's characters.
                - Only answer questions related to The Vampire Diaries.
                - If the question is not about The Vampire Diaries, respond with:
                " I can only answer questions about The Vampire Diaries!"
                - Do not answer non vampire diaries show related questions;
                - Modernization level should be high;
                - emoji level should be high;
                - Display response in proper lines or paragraphs or points, wherever looks readable and beautiful while read by user. It can be like chatgpt like response format;
                """;
        return ResponseEntity.ok(aiService.getResults(cid, prompt, systemMessage));
    }
}

