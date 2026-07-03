package org.spring.ai.aihub.slopGenerator.controller;

import org.spring.ai.aihub.slopGenerator.models.NewTweets;
import org.spring.ai.aihub.slopGenerator.service.SlopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlopController {

    @Autowired
    SlopService slopService;

    @GetMapping("/tweets")
    public NewTweets tweets(@RequestParam(defaultValue = "How to learn to program fast!") String originalTweet,
                            @RequestParam(defaultValue = "Spring AI") String topicHint,
                            @RequestParam(defaultValue = "high") String emojiLevel,
                            @RequestParam(defaultValue = "extreme") String modernizationLevel){

        return slopService.getTweetsResults(originalTweet, topicHint, modernizationLevel, emojiLevel);
    }
}
