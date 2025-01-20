package org.example;

import java.util.Random;

public class MessageProcessor {

    private String conversationState = "root";
    private int gameNumber;
    private String gameWord;
    char[] wordChars;
    Random randomizer = new Random();

    public String processCommand(String message) {
        String result = handleMessage(message);
//        conversationState = changeState(message);
        return result;
    }

    private String handleMessage(String message) {
        if (specialCommandCheck(message)) {
            conversationState = "root";
            return "Welcome to GuesserBot! Type '/help' for commands.";
        }

        return switch (conversationState) {
            case "root" -> rootConversation(message);
            case "numbers" -> numberGuess(message);
            case "words" -> wordGuess(message);
            default -> "An error occurred (impossible state)";
        };
    }

    private String changeState(String userMessage) {
        return switch (userMessage.toLowerCase()) {
            case "/numguesser" -> "numbers";
            case "/wordguesser" -> "words";
            case "/exit" -> "root";
            default -> conversationState;
        };
    }

    private String rootConversation(String message) {
        return switch (message) {
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
        gameWord = "preach";
        wordChars = gameWord.toCharArray();
        return String.format("I have a word, it's %d letters long. Take your guess!", wordChars.length);
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

        // Check what symbols coincide and if words are the same
        if (userGuess.length() == gameWord.length()) {
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

    private boolean specialCommandCheck(String str) {
        return str.equals("/exit");
    }
}
