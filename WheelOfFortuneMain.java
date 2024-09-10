import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class WheelOfFortuneMain {
    public static void main(String[] args) {
        WheelOfFortuneMain wheelOfFortune = new WheelOfFortuneMain();

        List<String> phraseList=null;
        // Get the phrase from a file of phrases
        try {
            phraseList = Files.readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }

        // Get a random phrase from the list
        Random rand = new Random();
        int r= rand.nextInt(3); // gets 0, 1, or 2
        String word = phraseList.get(r);
        word = word.toLowerCase();
        

        StringBuilder hiddenWord = new StringBuilder();

        System.out.println("How To Play: Guess individual letters until you reveal the word/phrase. You will be given 5 incorrect guesses before you lose.");

        //turns original string into a hidden string

        for (int i=0; i<word.length(); i++){
            char ch = word.charAt(i);
            if (Character.isLetter(ch)){
                hiddenWord.append("*");
            }
            else {
                hiddenWord.append(ch);
            }
        }
        
        System.out.println("Your Hidden Word Is: " + hiddenWord.toString());

        //allLettersGuessed = false; we have not solved the whole thing yet
        boolean allLettersGuessed = false;

        //found character = false; we have not found a character yet
        boolean found = false;

        //dupe character = false; we have not found a duplicate yet

        boolean dupe = false;

        int amountofGuesses = 5;

        String previousGuesses = "";

        while (!allLettersGuessed && amountofGuesses>-1) {

            found = false;
            dupe = false;
            
            //user inputs first guess 

            System.out.println("Guess A Letter: ");
        
            Scanner scanner = new Scanner(System.in);
            String letterGuess = scanner.nextLine();

            //Check if user has entered invalid input
            if (letterGuess.length() != 1 || !Character.isLetter(letterGuess.charAt(0))) {
                System.out.println("Invalid Input.");
                continue; // Skip the rest of the loop and ask for input again
            }

            //Check if user has already guessed the letter or not
            for (int n = 0; n < previousGuesses.length(); n++) {
                if (letterGuess.charAt(0) == previousGuesses.charAt(n)) {
                    dupe = true;
                }
            }
            
            // After the loop, check for duplicate status
            if (dupe == true) {
                System.out.println("You Already Guessed This Letter.");
            } 
            else {
                previousGuesses += letterGuess.charAt(0); // Add the guessed letter if it's not a duplicate
            }

            //Check if user has found the letter
            for (int i=0; i<word.length(); i++){
                char ch = word.charAt(i);
                if (ch==letterGuess.charAt(0)){
                    hiddenWord.setCharAt(i, ch);
                    found = true;
                }
              
            }

            if (found == true && dupe == false){
                System.out.println("You Found A Letter: " + hiddenWord.toString());
            }
            
            //Check if user has made an incorrect guess
            if (found == false && dupe == false){
                amountofGuesses = amountofGuesses - 1;
                if (amountofGuesses != -1){
                    System.out.println("Try Again. You have " + amountofGuesses + " incorrect guesses left");
                }
                
            }

            //once system hits this, it will loop back to "guess a letter"
            
            allLettersGuessed = !hiddenWord.toString().contains("*");
        }

        if (amountofGuesses == -1){
            System.out.println("You Lose!");
        }
        else{
            System.out.println("Congratulations! You Win!");
        }
}
}

