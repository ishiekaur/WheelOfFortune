import java.io.IOException;
import static java.nio.file.Files.readAllLines;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class WheelOfFortuneStatic {

    // Get a random phrase from the list
    public static String randomPhrase(List<String> phraseList) {
        Random rand = new Random();
        int r = rand.nextInt(3); // gets 0, 1, or 2
        return phraseList.get(r);
    }

    // Make the phrase hidden
    public static String generateHiddenPhrase(String secret, StringBuilder hiddenPhrase) {
        for (int i = 0; i < secret.length(); i++) {
            char ch = secret.charAt(i);
            if (Character.isLetter(ch)) {
                hiddenPhrase.append("*");
            } else {
                hiddenPhrase.append(ch);
            }
        }
        return hiddenPhrase.toString();
    }

    public static String getGuess() {
        System.out.println("Guess A Letter: ");
        Scanner scanner = new Scanner(System.in);
        String letterGuess = scanner.nextLine();
        return letterGuess.toLowerCase(); // Convert to lowercase for consistency
    }

    public static Boolean processGuess(String secret, String letterGuess, StringBuilder previousGuesses, StringBuilder hiddenPhrase) {
        boolean dupe = false;
        boolean found = false;

        // Check for duplicate guesses
        for (int n = 0; n < previousGuesses.length(); n++) {
            if (letterGuess.charAt(0) == previousGuesses.charAt(n)) {
                dupe = true;
            }
        }

        // If it's not a duplicate, add the guessed letter to previous guesses
        if (!dupe) {
            previousGuesses.append(letterGuess.charAt(0));
        }

        // Process the guess: update hidden phrase if the letter is found
        for (int i = 0; i < secret.length(); i++) {
            char ch = secret.charAt(i);
            if (ch == letterGuess.charAt(0)) {
                hiddenPhrase.setCharAt(i, ch);
                found = true;
            }
        }

        return found; // Return whether the guess was found in the phrase
    }

    public static void main(String[] args) {

        System.out.println("How To Play: Guess individual letters until you reveal the word/phrase. You will be given 5 incorrect guesses before you lose.");

        // Generate phrases
        List<String> phraseList = null;
        try {
            phraseList = readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }

        // Get a random phrase and prepare the hidden version
        String phrase = randomPhrase(phraseList);
        phrase = phrase.toLowerCase();

        // Create the hidden version of the phrase
        StringBuilder hiddenPhrase = new StringBuilder();
        generateHiddenPhrase(phrase, hiddenPhrase);
        System.out.println("Your Hidden Phrase Is: " + hiddenPhrase);

        // Track previous guesses
        StringBuilder previousGuesses = new StringBuilder();

        // Setup the game loop
        boolean allLettersGuessed = false;
        int amountOfGuesses = 5;

        while (!allLettersGuessed && amountOfGuesses > 0) {
            String myguess = getGuess();

            // Validate input (must be a single letter)
            if (myguess.length() != 1 || !Character.isLetter(myguess.charAt(0))) {
                System.out.println("Invalid Input.");
                continue; // Skip to the next iteration
            }

            // Process the guess
            Boolean found = processGuess(phrase, myguess, previousGuesses, hiddenPhrase);

            // If the guess was a duplicate
            if (previousGuesses.indexOf(myguess) != previousGuesses.length() - 1) {
                System.out.println("You Already Guessed This Letter.");
                continue;
            }

            // If the guess was correct
            if (found) {
                System.out.println("You Found A Letter: " + hiddenPhrase);
            } else {
                amountOfGuesses--;
                System.out.println("Incorrect Guess. You have " + amountOfGuesses + " incorrect guesses left.");
            }

            // Check if all letters are guessed
            allLettersGuessed = !hiddenPhrase.toString().contains("*");
        }

        // End of game
        if (amountOfGuesses <= 0) {
            System.out.println("You Lose! The phrase was: " + phrase);
        } else {
            System.out.println("Congratulations! You Win!");
        }
    }
}
