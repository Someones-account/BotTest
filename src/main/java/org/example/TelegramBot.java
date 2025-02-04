package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;
    private TextProcessor processor = new TextProcessor();

    public TelegramBot() {
        super("8052212937:AAE2yEkX1t85Zjca1sfxW3ZMvzbJBf9XZCo");
        this.botName = "Java5575Bot";
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return "Java5575Bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Process obtained message
            Message message = update.getMessage();
            String userMessage = message.getText();
            String chatId = message.getChatId().toString();
            String response = handleUserMessage(userMessage, message);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(response);

            try {
                execute(sendMessage); // Sending the message
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String handleUserMessage(String userMessage, Message fullMessage) {
        return processor.handleMessage(userMessage.toLowerCase(),fullMessage);
    }
}
