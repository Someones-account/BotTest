package org.example;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Random;

public class TextProcessor {

    private String conversationState = "root";
    private int gameNumber;
    private String gameWord;
    char[] wordChars;
    Random randomizer = new Random();

    String[] easyWords = {"game", "luck", "seek", "guide", "leap", "tree", "low", "great", "queue"};
    String[] mediumWords = {"strike", "betrayal", "meadow", "mirror", "zealot", "castle"};
    String[] complexWords = {"intimidate", "selector", "collapse", "gambling", "technology"};

    public String handleMessage(String messageText, Message message) {
        if (specialCommandCheck(messageText)) {
            conversationState = "root";
            return "Welcome to GuesserBot! Type '/help' for commands.";
        }

        return switch (conversationState) {
            case "root" -> rootConversation(messageText, message);
            case "numbers" -> numberGuess(messageText);
            case "words" -> wordGuess(messageText);
            default -> "An error occurred (impossible state)";
        };
    }

    private String rootConversation(String textMessage, Message message) {
        return switch (textMessage) {
            case "/start" -> "Welcome to GuesserBot! Type '/help' for commands.";
            case "/help" -> """
                    Available commands:
                    /start - Start the bot
                    /hello - Say hello
                    /numguesser - play a number guessing game
                    /wordguesser - play a word guessing game
                    /exit - leave current game
                    /help - Show this help message""";
            case "/hello" -> "Hello there! How can I assist you today?";
            case "/numguesser" -> initNumGame();
            case "/wordguesser" -> initWordGame();
            case "/user" -> userInfo(message);
            default -> "Sorry, I don't understand that command. Type '/help' for a list of commands.";
        };
    }

    private String initNumGame() {
        // Start number guessing game
        conversationState = "numbers";
        gameNumber = randomizer.nextInt(999) + 1;
        return "Okay, I chose a number between 1 and 1000. What is it? :)";
    }

    private String initWordGame() {
        // word randomizer
        conversationState = "words";
        gameWord = easyWords[randomizer.nextInt(easyWords.length)];
        wordChars = gameWord.toCharArray();
        return String.format("I have a word, it's %d letters long. Take your guess! (Type /easy , /medium , or /complex to change difficulty)", wordChars.length);
    }

    private String numberGuess(String guess) {
        int intGuess;
        // Check if input is valid
        try {
            intGuess = Integer.parseInt(guess);
        } catch (NumberFormatException e) {
            return "Please enter a number";
        }

        // Comparison
        if (intGuess != gameNumber) {
            return intGuess <= gameNumber ? "It's bigger" : "It's less";
        } else {
            conversationState = "root";
            return "Correct!";
        }
    }

    private String wordGuess(String userGuess) {
        boolean wordGuessed = true;
        StringBuilder comparisonResult = new StringBuilder();

        // Check if user changed game difficulty
        if (difficultySet(userGuess)) {
            wordChars = gameWord.toCharArray();
            return String.format("A new word of chosen difficulty was set. It is %d letters long", gameWord.length());
        }

        // Check what symbols coincide and if words are the same
        if (userGuess.length() == gameWord.length() && userGuess.matches("[a-zA-Z]+")) {
            char[] userChars = userGuess.toCharArray();
            for (int i = 0; i < userChars.length; i++) {
                if (userChars[i] == wordChars[i]) {
                    comparisonResult.append(userChars[i]);
                } else {
                    comparisonResult.append("-");
                    wordGuessed = false;
                }
            }
        } else {
            return String.format("Please, enter a word of length %d", gameWord.length());
        }

        // Check final state and return corresponding result
        if (wordGuessed) {conversationState = "root";}
        return (wordGuessed ? "You guessed it! The word was " + gameWord :
                String.format("Not quite! Here is what you've guessed: %s", comparisonResult));
    }

    private String userInfo(Message message) {
        Chat chat = message.getChat();
        return String.format("""
                Profile Name: %s
                Username: %s""", chat.getFirstName(), chat.getUserName());
    }

    private boolean difficultySet(String command) {
        switch (command) {
            case "/easy" -> gameWord = easyWords[randomizer.nextInt(easyWords.length)];
            case "/medium" -> gameWord = mediumWords[randomizer.nextInt(mediumWords.length)];
            case "/complex" -> gameWord = complexWords[randomizer.nextInt(complexWords.length)];
            default -> {
                return false;
            }
        }
        return true;
    }

    private boolean specialCommandCheck(String str) {
        return str.equals("/exit");
    }
}
