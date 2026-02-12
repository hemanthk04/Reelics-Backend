package com.projects.reelics.telegram;

import com.projects.reelics.models.Track;
import com.projects.reelics.services.TelegramInputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final TelegramInputService telegramInputService;
    private final TelegramCommandHandler commandHandler;

    private final String botUsername;
    private final long allowedUserId;

    public TelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            @Value("${telegram.allowed.user.id}") long allowedUserId,
            TelegramInputService telegramInputService,
            TelegramCommandHandler commandHandler
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.allowedUserId = allowedUserId;
        this.telegramInputService = telegramInputService;
        this.commandHandler = commandHandler;

        log.info("🤖 TelegramBot bean created");
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("TELEGRAM UPDATE RECEIVED");
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        long fromUserId = update.getMessage().getFrom().getId();

        if (fromUserId != allowedUserId) {
            log.warn("Unauthorized Telegram user tried to access bot: {}", fromUserId);
            return;
        }
        String text = update.getMessage().getText().trim();
        long chatId = update.getMessage().getChatId();

        log.info("Telegram message received: {}", text);

        try {
            // ───── /start ─────
            if (TelegramMessageParser.isStartCommand(text)) {
                send(chatId, commandHandler.handleStart());
                return;
            }

            // ───── /help ─────
            if (TelegramMessageParser.isHelpCommand(text)) {
                send(chatId, commandHandler.handleHelp());
                return;
            }


            // ───── /status <trackId> ─────
            if (TelegramMessageParser.isStatusCommand(text)) {
                UUID trackId = TelegramMessageParser.extractTrackId(text);
                send(chatId, commandHandler.handleStatus(trackId));
                return;
            }

            // ───── /mytracks ─────
            if (TelegramMessageParser.isMyTracksCommand(text)) {
                send(chatId, commandHandler.handleMyTracks());
                return;
            }

            // ───── Default: Instagram URL ─────
            Track track = telegramInputService.submitInstagramURL(text);

            send(chatId, """
                    Link accepted!
                    
                    Track ID:
                    %s
                    
                    Processing started.
                    Use:
                    /status %s
                    """.formatted(track.getId(), track.getId()));

        } catch (IllegalArgumentException e) {
            send(chatId, "⚠️ " + e.getMessage());
        } catch (Exception e) {
            log.error("Telegram bot error", e);
            send(chatId, " Something went wrong. Please try again later.");
        }
    }

    private void send(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram message", e);
        }
    }
}
