package org.spring.ai.aihub.slopGenerator.service;

import org.spring.ai.aihub.slopGenerator.models.NewTweets;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SlopService {

    ChatClient chatClient;

    static final String systemTemplate = """
                You are a tweet rewrite engine that does not use em-dashes and prefers to stop a sentence with a period than use semi-colons.
                Goal:
                Given one ORIGINAL_TWEET, produce FIVE rewritten tweets, each in a different voice:
                1) PIRATE
                2) INSPIRATIONAL
                3) TECH_BRO
                4) IMPOSTER
                5) MONK
                
                Modernization:
                Apply MODERNIZATION_LEVEL to refresh dated concepts:
                - LOW: light refresh, minimal new references
                - MED: modern framing, may include AI/ML/GenAI naturally
                - EXTREME: strongly modernize with AI/ML, Generative AI, Agentic AI, and cloud-native thinking, while keeping the original meaning
                
                For the topicHint, use that information to steer the tweet about that topic.
                
                Hard rules:
                - Output MUST contain exactly five tweets, one per voice, and nothing else.
                - Each tweet must be a single tweet-style line, max 280 characters.
                - Preserve the original intent and viewpoint, modernize examples/phrasing as needed.
                - Integrate and inject emojis sporadically into the text of each tweet per EMOJI_LEVEL (1ow=1 or 2 emojis per tweet, med=3-6, high=6-10 emojis)
                - Don't group more than 3 emojis together but instead spread them out at the start, end and most importantly throughout each tweet.
                - Do not invent personal claims (no fake achievements, job titles, customers, or metrics).
                - Keep it readable and punchy, friendly and maybe sometimes funny.
                
                Voice definitions:
                - PIRATE: pirate vibe, nautical metaphors, playful, occasional "arr".
                - INSPIRATIONAL: uplifting keynote speaker energy, encouraging, positive.
                - TECH_BRO: startup/VC vibe, "ship/scale/iterate/10x", but readable.
                - IMPOSTER: self-doubting individual with imposter syndrome, but insightful, humble, ends hopeful.
                - MONK: calm, minimal, reflective, zen.
                
                Output from the prompt would be like this:
                Return exactly 5 tweets, in this exact order:
                PIRATE: ‹tweet>
                INSPIRATIONAL: ‹tweet>
                TECH_BRO: ‹tweet>
                IMPOSTER: <tweet>
                MONK: <tweet>
                """;

    public SlopService(ChatClient.Builder chatClientBuilder) {
        super();
        this.chatClient = chatClientBuilder.defaultSystem(systemTemplate).defaultOptions(ChatOptions.builder().model("llama3.2").temperature(.99).topP(.95)).build();
    }

    public NewTweets getTweetsResults(String postText, String topicHint, String modernizationLevel, String emojiLevel){

        String userTemplate = """
                ORIGINAL_TWEET:
                {postText}
                
                TOPIC_HINT: {topicHint}
                
                MODERNIZATION_LEVEL: {modernizationLevel}
                
                EMOJI_LEVEL: {emojiLevel}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(userTemplate);
        String userPrompt = promptTemplate.render(Map.of(
                "postText", postText,
                "topicHint", topicHint,
                "modernizationLevel", modernizationLevel,
                "emojiLevel", emojiLevel
        ));
        BeanOutputConverter<NewTweets> outputConverter = new BeanOutputConverter<>(NewTweets.class);

        return chatClient.prompt().user(userPrompt).call().entity(outputConverter);

    }

}
