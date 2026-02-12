package com.projects.reelics.telegram;

import java.util.UUID;

public class TelegramMessageParser {

    public static boolean isStatusCommand(String text) {
        return text.startsWith("/status");
    }

    public static boolean isMyTracksCommand(String text) {
        return text.equals("/mytracks");
    }

    public static boolean isStartCommand(String text) {
        return text.equals("/start");
    }

    public static boolean isHelpCommand(String text) {
        return text.equals("/help");
    }

    public static boolean isCommand(String text) {
        return text.startsWith("/");
    }


    public static UUID extractTrackId(String text) {
        String[] parts = text.split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Track ID missing");
        }
        return UUID.fromString(parts[1]);
    }
}
