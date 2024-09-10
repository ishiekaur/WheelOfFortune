import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class WheelOfFortuneBot {

    // Method to generate a random letter from 'a' to 'z'
    public static char getRandomLetter() {
        Random random = new Random();
        // Generate a random number between 0 and 25 (inclusive)
        int randomNumber = random.nextInt(26);
        // Convert the random number to a corresponding letter
        return (char) ('a' + randomNumber);
    }

    public static void main(String[] args) {
        // Create a bot instance
        WheelOfFortuneBot bot = new WheelOfFortuneBot();

        List<String> phraseList = null;
        // Get the phrase from a file of phrases
        try {
            phraseList = Files.readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        // Get a random phrase from the list
        Random rand = new Random();
        int r = rand.nextInt(phraseList.size()); // gets a random index within the list size
        String word = phraseList.get(r);
        word = word.toLowerCase();

        StringBuilder hiddenWord = new StringBuilder();

        System.out.println("How To Play: The bot will guess letters to reveal the word/phrase. The bot will be given 5 incorrect guesses before it loses.");

        // Turns original string into a hidden string
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (Character.isLetter(ch)) {
                hiddenWord.append("*");
            } else {
                hiddenWord.append(ch);
            }
        }

        System.out.println("Your Hidden Word Is: " + hiddenWord.toString());

        boolean allLettersGuessed = false;
        int amountofGuesses = 5;

        StringBuilder previousGuesses = new StringBuilder();

        while (!allLettersGuessed && amountofGuesses > 0) {

            boolean found = false;
            boolean dupe = false;

            // Bot makes a guess
            char letterGuess = getRandomLetter();
            String letterGuessStr = String.valueOf(letterGuess);

            // Check if bot has already guessed the letter
            if (previousGuesses.toString().contains(letterGuessStr)) {
                dupe = true;
            }

            if (dupe) {
                System.out.println("Bot already guessed this letter: " + letterGuessStr);
                continue; // Skip the rest of the loop and make another guess
            }

            previousGuesses.append(letterGuessStr); // Add the guessed letter

            // Check if the bot has found the letter
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                if (ch == letterGuess) {
                    hiddenWord.setCharAt(i, ch);
                    found = true;
                }
            }

            if (found) {
                System.out.println("Bot found a letter: " + hiddenWord.toString());
            } else {
                amountofGuesses--;
                System.out.println("Bot made an incorrect guess. " + amountofGuesses + " incorrect guesses left.");
            }

            allLettersGuessed = !hiddenWord.toString().contains("*");
        }

        if (amountofGuesses <= 0) {
            System.out.println("Bot Loses!");
        } else {
            System.out.println("Congratulations! The bot wins!");
        }
    }
}
