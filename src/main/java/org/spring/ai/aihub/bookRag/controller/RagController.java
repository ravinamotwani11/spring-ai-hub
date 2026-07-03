package org.spring.ai.aihub.bookRag.controller;

import jakarta.annotation.PostConstruct;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class RagController {

    ChatClient chatClient;
    VectorStore vectorStore;
    ChatMemory chatMemory;

    public RagController(ChatClient.Builder builder, EmbeddingModel embeddingModel, ChatMemory chatMemory) {
        MessageChatMemoryAdvisor memoryAdvisors = MessageChatMemoryAdvisor.builder(chatMemory).build();
        chatClient = builder.defaultAdvisors(memoryAdvisors).build();
        vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        this.chatMemory = chatMemory;
    }

    @PostConstruct
    public void ingestBookOnStartup() throws Exception {
        String pdfUrl = "https://certificationexams.pro/docs/pickeringisspringfield.pdf";
        UrlResource urlResource = new UrlResource(URI.create(pdfUrl));
        List<Document> pages = new PagePdfDocumentReader(urlResource).get();
        vectorStore.add(pages);
        System.out.println("RAG ingestion complete: " + pdfUrl);
    }

    @GetMapping("/askAgain")
    public String askAgain(@RequestParam String cid, @RequestParam String question) {
        String userMessage = generateAugmentedPrompt(question);
        ChatClient.ChatClientRequestSpec prompt = chatClient.prompt().user(userMessage);
        ChatClient.ChatClientRequestSpec advisedPrompt = prompt.advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, cid));
        return advisedPrompt.call().content();
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String cid, @RequestParam String question) {

        chatMemory.add(cid, new UserMessage(question));

        String prompt = generateAugmentedPrompt(question);

        List<Message> historyMessages = chatMemory.get(cid);

        String generatedContent = chatClient.prompt(prompt).messages(historyMessages).call().content();

        chatMemory.add(cid, new SystemMessage(generatedContent));
        return generatedContent;

    }

    private @NonNull String generateAugmentedPrompt(String question) {
        SearchRequest retrievalQuery = SearchRequest.builder().query(question).topK(5).build();
        List<Document> retrievedPages = vectorStore.similaritySearch(retrievalQuery);

        StringBuilder augmentedContext = new StringBuilder();
        for (int i = 0; i < retrievedPages.size(); i++) {
            Document page = retrievedPages.get(i);
            augmentedContext.append("\n\n Page from the book: ").append(page.getText());
        }
        System.out.println(augmentedContext);

        String prompt = """
                You are a helpful assistant for answering questions about the book "Pickering and Springfield: 
                A History of the Railways of the Eastern Region of the Great Western Railway". 
                Use the following retrieved pages from the book to answer the question. 
                If you don't know the answer, say you don't know or May be ask me a different question. 
                Always use only the retrieved pages as context for answering, never any external information."
                %s
                Question: %s
                Answer:""".formatted(augmentedContext.toString(), question);
        return prompt;
    }

}
