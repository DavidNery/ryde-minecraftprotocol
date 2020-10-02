package com.github.davidnery.rydeminecraftprotocol.utils;

import com.github.davidnery.rydeminecraftprotocol.enums.ChatColor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class ChatUtils {

    public static TextFlow parseMessage(JsonElement message) {
        TextFlow textFlow = new TextFlow();
        if (message instanceof JsonArray)
            ((JsonArray) message).forEach(text -> parseText(textFlow, text, null));
        else
            parseText(textFlow, message, null);

        return textFlow;
    }

    private static void parseText(TextFlow textFlow, JsonElement text, ChatColor color) {
        // https://wiki.vg/Chat#Schema

        // Samples
        // {"translate":"chat.type.announcement","with":["Server",{"extra":["test"],"text":""}]}
        // {"extra":[{"color":"yellow","text":"Notch joined the game"}],"text":""}
        if (text instanceof JsonObject) {
            if (((JsonObject) text).has("text")) {
                String generalMessage = ((JsonObject) text).get("text").getAsString();
                if (!generalMessage.isEmpty()) {
                    Text textComponent = new Text(generalMessage);
                    parseStyle(textComponent, (JsonObject) text);
                    textFlow.getChildren().add(textComponent);
                }
            }
            if (((JsonObject) text).has("translate")) {
                ((JsonObject) text).getAsJsonArray("with").forEach(extraText ->
                        parseText(textFlow, extraText, ChatColor.LIGHT_PURPLE)
                );
            }
            if (((JsonObject) text).has("extra")) {
                ((JsonObject) text).getAsJsonArray("extra").forEach(extraText ->
                        parseText(textFlow, extraText, color)
                );
            }
        } else {
            Text textComponent = new Text(text.getAsString() + " ");
            if (color != null)
                textComponent.setFill(Color.web(color.getColor()));
            else
                textComponent.setFill(Color.WHITE);
            textFlow.getChildren().add(textComponent);
        }
    }

    private static void parseStyle(Text text, JsonObject textObject) {
        text.setFont(Font.font("Verdana",
                textObject.has("bold") ? FontWeight.BOLD : FontWeight.NORMAL,
                textObject.has("italic") ? FontPosture.ITALIC : FontPosture.REGULAR,
                12
        ));

        if (textObject.has("underlined") && textObject.get("underlined").getAsBoolean())
            text.setUnderline(true);
        if (textObject.has("strikethrough") && textObject.get("strikethrough").getAsBoolean())
            text.setStrikethrough(true);

        if (textObject.has("color")) {
            Color color = Color.web(ChatColor.parse(textObject.get("color").getAsString()).getColor());
            text.setFill(color);
            text.setStroke(color);
            text.setStrokeWidth(1);
        }
    }

}
