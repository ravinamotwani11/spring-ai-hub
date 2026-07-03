package org.spring.ai.aihub.foodMenu;

import org.spring.ai.aihub.foodMenu.models.Menu;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/menu")
public class FoodMenuController {

    ChatClient chatClient;
    ImageModel imageModel;
    TextToSpeechModel textToSpeechModel;

    public FoodMenuController(ChatClient.Builder builder, ImageModel imageModel, TextToSpeechModel textToSpeechModel) {
        super();
        this.chatClient = builder.build();
        this.imageModel = imageModel;
        this.textToSpeechModel = textToSpeechModel;
    }

    @GetMapping(value = "/menu/text", produces = MediaType.TEXT_PLAIN_VALUE)
    public String convertToText() {
        var menuImage = new FileSystemResource("/menu.jpeg");
        String systemPrompt = """
                You are an OCR engine.
                Extract ALL readable text from the provided image.
                Return ONLY the raw text.
                Preserve line breaks where possible.
                """;

        String userPrompt = "Run OCR on this menu image";
        var prompt = chatClient.prompt().system(systemPrompt).user(u -> u.text(userPrompt).media(MimeTypeUtils.IMAGE_JPEG, menuImage));
        return prompt.call().content();
    }

    @GetMapping(value = "/menu/english", produces = MediaType.TEXT_HTML_VALUE)
    public String convertToEnglish() {
        var ocrText = convertToText();
        String englishText = "";
        var systemPrompt = """
                You are a professional menu translator.
                Translate the user's text into English.
                Keep the formatting (line breaks, heading, prices) as close as possible.
                Do not add extra explanations.
                Return the menu in HTML format for easy rendering.
                """;
        var prompt = chatClient.prompt().system(systemPrompt).user(ocrText);
        englishText = prompt.call().content();
        return englishText;
    }

    @GetMapping(value = "/menu/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Menu convertToJson() {
        var englishText = convertToEnglish();
        var systemPrompt = """
                Convert the menu text into JSON that matches the provided schema.
                Rules:
                - restaurantName: best guess from headings; if unknown use "Unknown Restaurant"
                - sourceLanguage: best guess if you can infer; else "Unknown"
                - targetLanguage: always "English"
                - items: include only actual menu items (not section headings)
                - price. amount: numeric as a string, e.g. "12.50" or "12"
                - description: empty string if missing
                Return ONLY valid JSON (no markdown, no commentary).
                """;
        var userPrompt = """
                Here is the translated menu text. Convert it to structured JSON:
                %s
                """.formatted(englishText);
        var prompt = chatClient.prompt().system(systemPrompt).user(userPrompt);
        return prompt.call().entity(Menu.class);
    }

    @GetMapping(value = "menu/image", produces = MediaType.TEXT_HTML_VALUE)
    public String generateImageFromMenu() {
        Menu menu = convertToJson();
        var prompt = """
                Create a beautiful image (1024x1792) inspired by the restaurant and dishes in this text file %s.
                
                Critical requirements: - Make it visually striking and professional.
                
                Style guidance (choose ONE, not both):
                - Option A: cinematic food photography aesthetic, warm lighting, shallow depth of field.
                - Option B: elegant minimal illustration, premium bistro vibe.
                
                Composition:
                - strong focal point (plate / ingredients / table setting)
                - high contrast
                - uncluttered
                """.formatted(menu.toString());

        if (prompt.length() > 3750) {
            prompt = prompt.substring(0, 3750);
        }
        var options = OpenAiImageOptions.builder().model("dall-e-3").width(1024).height(1792).responseFormat("b64_json").n(1).build();
        ImageResponse response = imageModel.call(new ImagePrompt(prompt, options));
        var base64Representation = Objects.requireNonNull(response.getResult()).getOutput().getB64Json();
        var html = """
                < img alt="Restaurant hero poster" style="max-width: 900px; width: 100%%; border: 1px solid #ccc;" src="data:image/png;base64,%s"/>
                """.formatted(base64Representation);
        return html;
    }

    @GetMapping(value = "/menu/english/audio", produces = "audio/mpeg")
    public byte[] englishAudio() throws Exception {
        Menu menu = convertToJson();
        var textToSpeak = "Here are the menu items " + menu.getMenuItems();
        if (textToSpeak.length() > 3750) {
            textToSpeak = textToSpeak.substring(0, 3750);
        }
        var prompt = new TextToSpeechPrompt(textToSpeak);
        var response = textToSpeechModel.call(prompt);
        return response.getResult().getOutput();
    }
}
