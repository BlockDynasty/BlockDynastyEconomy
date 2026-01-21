package com.BlockDynasty.hytale.adapters;

import com.BlockDynasty.hytale.adapters.Materials.Colors;
import com.hypixel.hytale.server.core.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageAdapter {
    private static final Pattern COLOR_PATTERN = Pattern.compile("(§[0-9a-fA-Fk-oK-OrR])");

    public static Message formatVanillaMessage(String message) {
        if (message == null || message.isEmpty()) {
            return Message.raw("");
        }

        List<Message> messages = new ArrayList<>();
        Matcher matcher = COLOR_PATTERN.matcher(message);

        int lastEnd = 0;
        Color currentColor = Color.WHITE;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String text = message.substring(lastEnd, matcher.start());
                messages.add(Message.raw(text).color(currentColor));
            }

            currentColor = Colors.decode(matcher.group(1));
            lastEnd = matcher.end();
        }

        if (lastEnd < message.length()) {
            messages.add(Message.raw(message.substring(lastEnd)).color(currentColor));
        }

        return messages.isEmpty() ? Message.raw("") : Message.join(messages.toArray(new Message[0]));
    }

    public static Message formatVanillaMessage(String[] stringlist){
        List<Message> messages = new ArrayList<>();
        for (String s : stringlist) {
            messages.add(formatVanillaMessage(s));
        }
        return Message.join(messages.toArray(new Message[0]));
    }

    public static String clearColorCodes(String message) {
        return message.replaceAll("§[0-9a-fA-Fk-oK-OrR]", "");
    }

    public static Message formatModernMessage(String message) {
        return Message.raw(message);
    }
}
