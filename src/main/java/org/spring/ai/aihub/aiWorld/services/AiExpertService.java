package org.spring.ai.aihub.aiWorld.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiExpertService {

    ChatClient chatClient;
    ChatMemory chatMemory;

    public AiExpertService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder.build();
        this.chatMemory = chatMemory;
    }

    public String getResults(String cid, String prompt, String systemMessage) {
        chatMemory.add(cid, new UserMessage(prompt));

        List<Message> historyMessages = chatMemory.get(cid);

        var generatedContent = chatClient.prompt().system(systemMessage).user(prompt)
                .messages(historyMessages).call().content();

        chatMemory.add(cid, new SystemMessage(generatedContent));

        return generatedContent;
    }
}
