import java.io.IOException;
import static java.nio.file.Files.readAllLines;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class WheelOfFortuneOOP{
    public String phrase;
    public StringBuilder hiddenPhrase = new StringBuilder();
    public String previousGuesses = ""; // Initialize to track guesses

    public void randomPhrase(List<String> phraseList){
        // Get a random phrase from the list
        Random rand = new Random();
        int r= rand.nextInt(3); // gets 0, 1, or 2
        this.phrase = phraseList.get(r);
    }

    public void generateHiddenPhrase(){
        for (int i=0; i<this.phrase.length(); i++){
            char ch = this.phrase.charAt(i);
            if (Character.isLetter(ch)){
                this.hiddenPhrase.append("*");
            }
            else {
                this.hiddenPhrase.append(ch);
            }
        }
    }

    public String getGuess(){
        System.out.println("Guess A Letter: ");
        Scanner scanner = new Scanner(System.in);
        String letterGuess = scanner.nextLine().toLowerCase(); // Convert to lowercase for consistency

        // Check for invalid input: not a single letter
        if (letterGuess.length() != 1 || !Character.isLetter(letterGuess.charAt(0))) {
            System.out.println("Invalid Input.");
            return getGuess(); // Prompt again for valid input
        }

        // Check if the letter was already guessed
        if (previousGuesses.contains(letterGuess)) {
            System.out.println("You already guessed this letter.");
            return getGuess(); // Prompt again for a new guess
        }

        // Add the new guess to the list of previous guesses
        this.previousGuesses += letterGuess;
        return letterGuess;
    }

    public Boolean processGuess(String letterGuess) {
        boolean found = false;

        for (int i=0; i<this.phrase.length(); i++){
            char ch = this.phrase.charAt(i);
            if (ch == letterGuess.charAt(0)){
                this.hiddenPhrase.setCharAt(i, ch);
                found = true;
            }
        }
        return found;
    }

    public static void main(String[] args) {
        System.out.println("How To Play: Guess individual letters until you reveal the word/phrase. You will be given 5 incorrect guesses before you lose.");

        WheelOfFortuneOOP game = new WheelOfFortuneOOP();

        // Generate phrases
        List<String> phraseList = null;
        try {
            phraseList = readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }

        // Get random phrase
        game.randomPhrase(phraseList);
        game.phrase = game.phrase.toLowerCase();

        // Generate hidden phrase
        game.generateHiddenPhrase();
        System.out.println("Your Hidden Phrase Is: " + game.hiddenPhrase.toString());

        // Setup while loop
        boolean allLettersGuessed = false;
        int amountofGuesses = 5;

        while (!allLettersGuessed && amountofGuesses >= 0) {
            String myguess = game.getGuess();
            Boolean found = game.processGuess(myguess);

            if (found) {
                System.out.println("You Found A Letter: " + game.hiddenPhrase.toString());
            } else {
                amountofGuesses--;
                if (amountofGuesses >= 0) {
                    System.out.println("Incorrect Guess. You have " + amountofGuesses + " incorrect guesses left.");
                }
            }

            allLettersGuessed = !game.hiddenPhrase.toString().contains("*");
        }

        if (amountofGuesses <= 0) {
            System.out.println("You Lose!");
        } else {
            System.out.println("Congratulations! You Win!");
        }
    }
}
