package com.example.capston2.AI;

import com.example.capston2.Api.ApiException;
import com.example.capston2.Model.Catalog;
import com.example.capston2.Model.Designer;
import com.example.capston2.Model.Message;
import com.example.capston2.Repository.CatalogRepository;
import com.example.capston2.Repository.DesignerRepository;
import com.example.capston2.Repository.MessageRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AiService {

    private final MessageRepository messageRepository;
    private final DesignerRepository designerRepository;
    private final CatalogRepository catalogRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    // TEXT MODEL (proposal generation)
    private final String GEMINI_TEXT_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-lite-latest:generateContent?key=";

    // IMAGE MODEL (style analysis)
    private final String GEMINI_VISION_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite:generateContent?key=";

    // ---------------------------------------------------------
    // 1. Generate Proposal
    // ---------------------------------------------------------
    public String generateProposal(Integer chatRoomId) {

        List<Message> messages = messageRepository.findMessageByChatRoomId(chatRoomId);

        if (messages.isEmpty()) {
            return "No messages found for this chat room.";
        }

        StringBuilder text = new StringBuilder();
        for (Message m : messages) {
            text.append(m.getSenderName())
                    .append(": ")
                    .append(m.getContent())
                    .append("\n");
        }

        String prompt =
                "Summarize the following conversation between a client and designer. " +
                        "Extract all agreed points, requirements, price, and deadline. " +
                        "Write a clean professional proposal:\n\n" + text;

        return callGeminiText(prompt);
    }

    // ---------------------------------------------------------
    // 2. Find Designers by Style
    // ---------------------------------------------------------
    public List<Designer> findDesignersByStyle(String imageUrl) throws Exception {
        byte[] referenceImageBytes = downloadImage(imageUrl);
        return findDesignersByStyle(referenceImageBytes);
    }

    public List<Designer> findDesignersByStyle(byte[] referenceImageBytes) throws Exception {

        String referenceStyle = analyzeImage(referenceImageBytes);

        List<Designer> designers = designerRepository.findAll();
        List<DesignerScore> scores = new ArrayList<>();

        for (Designer d : designers) {

            if (d.getCatalogs() == null || d.getCatalogs().isEmpty()) continue;

            Catalog catalog = catalogRepository.findCatalogById(d.getCatalogs().get(0));
            if (catalog == null) continue;

            String designerStyle = catalog.getAiStyle();

            if (designerStyle == null || designerStyle.isEmpty()) {
                byte[] bytes = downloadImage(catalog.getImages().get(0));
                designerStyle = analyzeImage(bytes);
                catalog.setAiStyle(designerStyle);
                catalogRepository.save(catalog);
            }

            double score = localSimilarity(referenceStyle, designerStyle);

            scores.add(new DesignerScore(d, score));
        }

        scores.sort((a, b) -> Double.compare(b.score, a.score));

        return scores.stream().map(s -> s.designer).toList();
    }

    // ---------------------------------------------------------
    // Download image bytes
    // ---------------------------------------------------------
    public byte[] downloadImage(String url) throws Exception {
        URL imageUrl = new URL(url);
        return imageUrl.openStream().readAllBytes();
    }

    // ---------------------------------------------------------
    // UNIVERSAL PARSER (works for all Gemini models)
    // ---------------------------------------------------------
    private String extractText(JsonObject obj) {

        JsonArray candidates = obj.getAsJsonArray("candidates");
        JsonObject firstCandidate = candidates.get(0).getAsJsonObject();

        JsonElement contentElement = firstCandidate.get("content");

        JsonObject firstContent;

        if (contentElement.isJsonArray()) {
            firstContent = contentElement.getAsJsonArray().get(0).getAsJsonObject();
        } else {
            firstContent = contentElement.getAsJsonObject();
        }

        JsonArray partsArray = firstContent.getAsJsonArray("parts");

        return partsArray.get(0).getAsJsonObject().get("text").getAsString();
    }

    // ---------------------------------------------------------
    // TEXT MODEL CALL
    // ---------------------------------------------------------
    private String callGeminiText(String prompt) {

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contentsArray);

        Request request = new Request.Builder()
                .url(GEMINI_TEXT_URL + apiKey)
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.body() == null) {
                return "AI Error: Empty response body.";
            }

            String json = response.body().string();
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

            if (obj.has("error")) {
                return "AI Error: " + obj.get("error").getAsJsonObject().get("message").getAsString()
                        + ". Full response: " + json;
            }

            return extractText(obj);

        } catch (Exception e) {
            return "AI Error: " + e.getMessage();
        }
    }

    // ---------------------------------------------------------
    // IMAGE MODEL CALL (Fixed with inlineData wrapper)
    // ---------------------------------------------------------
    public String analyzeImage(byte[] imageBytes) {

        JsonObject inlineData = new JsonObject();
        inlineData.addProperty("mimeType", "image/jpeg");
        inlineData.addProperty("data", Base64.getEncoder().encodeToString(imageBytes));

        JsonObject imagePart = new JsonObject();
        imagePart.add("inlineData", inlineData);

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", "Describe the artistic style of this image.");

        JsonArray parts = new JsonArray();
        parts.add(imagePart);
        parts.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contentsArray);

        Request request = new Request.Builder()
                .url(GEMINI_VISION_URL + apiKey)
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.body() == null) {
                return "AI Error: Empty response body.";
            }

            String json = response.body().string();
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

            if (obj.has("error")) {
                return "AI Error: " + obj.get("error").getAsJsonObject().get("message").getAsString()
                        + ". Full response: " + json;
            }

            return extractText(obj);

        } catch (Exception e) {
            return "AI Error: " + e.getMessage();
        }
    }

    // ---------------------------------------------------------
    // Local similarity scoring
    // ---------------------------------------------------------
    private double localSimilarity(String a, String b) {
        if (a == null || b == null) return 0.0;

        a = a.toLowerCase();
        b = b.toLowerCase();

        Set<String> wordsA = new HashSet<>(Arrays.asList(a.split("\\s+")));
        Set<String> wordsB = new HashSet<>(Arrays.asList(b.split("\\s+")));

        int shared = 0;
        for (String w : wordsA) {
            if (wordsB.contains(w)) shared++;
        }

        int maxSize = Math.max(wordsA.size(), wordsB.size());
        if (maxSize == 0) return 0.0;

        return (double) shared / maxSize * 100;
    }

    private static class DesignerScore {
        Designer designer;
        double score;

        DesignerScore(Designer designer, double score) {
            this.designer = designer;
            this.score = score;
        }
    }

    public Double extractPrice(String text) {
        try {
            Pattern pattern = Pattern.compile("(?i)(price|cost|total cost|agreed price)[^0-9]*(\\d+)");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                return Double.parseDouble(matcher.group(2));
            }

            Pattern fallback = Pattern.compile("\\$(\\d+)");
            Matcher m2 = fallback.matcher(text);
            if (m2.find()) {
                return Double.parseDouble(m2.group(1));
            }

        } catch (Exception ignored) {}

        return 0.0;
    }

    public LocalDate extractDeadline(String text) {
        try {
            Pattern pattern = Pattern.compile("(?i)(\\d+)\\s*days");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                int days = Integer.parseInt(matcher.group(1));
                return LocalDate.now().plusDays(days);
            }

        } catch (Exception ignored) {}

        return LocalDate.now();
    }

    public String cleanDetails(String text) {
        return text
                .replaceAll("\\*\\*\\*", "")
                .replaceAll("\\*\\*", "")
                .replaceAll("###", "")
                .replaceAll("####", "")
                .replaceAll("  \n", "\n")
                .replaceAll("\\n{3,}", "\n\n")
                .replaceAll("\\n{2,}", "\n")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }
}