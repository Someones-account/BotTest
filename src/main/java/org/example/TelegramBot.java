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
    private MessageProcessor processor = new MessageProcessor();

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
            String response = handleUserMessage(userMessage);


            // Send response back to user
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

    private String handleUserMessage(String userMessage) {
        return processor.processCommand(userMessage.toLowerCase());
    }
}


// difficulties for games

// return switch (userMessage.toLowerCase()) {
//            case "/start" -> "Welcome to MySimpleTelegramBot! Type '/help' for commands.";
//            case "/help" ->
//                    "Available commands:\n/start - Start the bot\n/hello - Say hello\n/help - Show this help message";
//            case "/hello" -> "Hello there! How can I assist you today?";
//            case "/numguesser" -> "Okay, I chose a number between 1 and 1000. What is it? :)";
//            default -> "Sorry, I don't understand that command. Type '/help' for a list of commands.";
//        };