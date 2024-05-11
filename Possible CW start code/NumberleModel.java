// NumberleModel.java

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public class NumberleModel extends Observable implements INumberleModel {
    private StringBuilder currentGuess;
    private int remainingAttempts;
    private boolean gameWon;
    private boolean validFlag = false;
    private boolean randomFlag = false;
    private boolean displayFlag = false;
    private String targetEquation;
    private final List<List<ColoredChar>> coloredChars = new ArrayList<>();
    ColorState colorState;
    GrayState grayState = new GrayState();
    GreenState greenState = new GreenState();
    OrangeState orangeState = new OrangeState();

    /**
     * @invariant remainingAttempts >= 0 && remainingAttempts <= 6
     * Initializes the model's state. Sets the target equation, resets the current guess, remaining attempts, and game state.
     * @requires targetEquation != null
     * @ensures currentGuess != null && remainingAttempts >= 0 && remainingAttempts <= 6
     * @post The model's state is initialized with the target equation,
     * the current guess set to "       ", the remaining attempts set to MAX_ATTEMPTS,
     * and the game state set to not won.
     */
    @Override
    public void initialize(){
        if(randomFlag){
            targetEquation = getRandomEquation();
        }
        else
            targetEquation = "6*1-2=4";
        if(displayFlag)
            System.out.println("Target Equation: " + targetEquation);
        currentGuess = new StringBuilder("       ");
        remainingAttempts = MAX_ATTEMPTS;
        assert remainingAttempts >= 0 && remainingAttempts <= 6 : "remainingAttempts should be between 0 and 6";
        assert currentGuess.length() == 7 : "Current guess length should be 7";
        gameWon = false;
        //Initialize coloredChars
        coloredChars.clear();
        // Observer
        setChanged();
        notifyObservers();
    }

    private String getRandomEquation() {
        ArrayList<String> equations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\Waitting\\Eng\\AOOP\\cw\\possible-cw-start-code\\Possible CW start code\\equations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                equations.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (equations.isEmpty()) {
            throw new RuntimeException("No equations found in the file");
        }

        Random rand = new Random();
        // Return a random equation from the list
        return equations.get(rand.nextInt(equations.size()));
    }


    /**
     * Calculates the result of the given equation.
     * @invariant res >= 0 && preVal >= 0
     * @requires s != null && s.length() > 0
     * @ensures \result == \old(calculator(s))
     * @pre The input string is not null
     * @post The result of the given equation is returned
     * @param s The equation to be calculated.
     * @return The result of the given equation.
     */
    public static int calculator(String s) {
        assert s != null : "Input string should not be null";
        // Add a '+' before and after for ease of processing
        s = "+" + s + "+";
        int res = 0, preVal = 0; // preVal is used to store the previous number
        char preOp = '+'; // preOp is used to save the previous operator
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // If it is a number, calculate the value of the current number
            if(Character.isDigit(c)) {
                int val = c - '0';
                // If the next character of the current character is also a number, it means that the current number has more than one digit
                while(i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                    val = val * 10 + (s.charAt(i + 1) - '0');
                    i++;
                }
                // Decide what to do with the current number based on the previous operator
                if (preOp == '+') { //
                    res += preVal;
                    preVal = val;
                } else if (preOp == '-') {
                    res += preVal;
                    preVal = -val;
                } else if (preOp == '*') {
                    preVal = preVal * val;
                } else if (preOp == '/') {
                    preVal = preVal / val;
                }
            } else {
                preOp = c;
            }
        }
        res += preVal;
        return res;
    }


    /**
     * @invariant equation.length() == 7
     * @requires equation != null
     * @ensures \result == true || \result == false
     * Processes the user's input. If the length of the input equation is 7 and the result of the equation is 8, updates the current guess and decreases the remaining attempts.
     * @param equation The user's input equation
     * @pre The equation is not null
     * @post If the input is valid, the current guess is updated to the input and the remaining attempts is decreased by 1. If the input is invalid, the current guess and the remaining attempts remain the same.
     * @return True if the input is valid, false otherwise
     */
    @Override
    public boolean processInput(String equation) {
        assert equation != null : "Input equation should not be null";

        boolean result = false;
        if(equation.length()==7){
            // split the equation into left and right parts
            String[] parts = equation.split("=");
            try {
                int leftValue = calculator(parts[0]);
                int rightValue = calculator(parts[1]);
                validFlag = leftValue == rightValue; // check if the equation is valid
            } catch (Exception e) {
                validFlag = false;
            }
            if(validFlag){
                remainingAttempts--;
                currentGuess = new StringBuilder(equation);
                // Observer
                setChanged();
                notifyObservers();
                result = true;
            }
        }
        return result;
    }


    /**
     * Checks if the game is over. The game is over if the remaining attempts is 0 or the game is won.
     * @return True if the game is over, false otherwise
     */
    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0 || gameWon;
    }

    /**
     * Checks if the game is won. The game is won if the current guess equals the target equation and the game is not over.
     * @return True if the game is won, false otherwise
     */
    @Override
    public boolean isGameWon() {
        assert currentGuess != null : "Current guess should not be null";
        gameWon = currentGuess.toString().equals(targetEquation) && !isGameOver();
        assert gameWon == true || gameWon == false : "GameWon should be either true or false";
        return gameWon;
    }

    /**
     * Gets the target equation.
     * @return The target equation
     */
    @Override
    public String getTargetEquation() {
        assert targetEquation != null : "Target equation should not be null";
        return targetEquation;
    }

    /**
     * Gets the current guess.
     * @return The current guess
     */
    @Override
    public StringBuilder getCurrentGuess() {
        assert currentGuess != null : "Current guess should not be null";
        return currentGuess;
    }

    /**
     * Gets the remaining attempts.
     * @return The remaining attempts
     */
    @Override
    public int getRemainingAttempts() {
        assert remainingAttempts >= 0 && remainingAttempts <= 6 : "remainingAttempts should be between 0 and 6";
        return remainingAttempts;
    }

    /**
     * Sets the flags.
     * @invariant randomFlag == true || randomFlag == false
     * @invariant displayFlag == true || displayFlag == false
     */
    public void setFlags(boolean randomFlag, boolean displayFlag) {
        this.randomFlag = randomFlag;
        this.displayFlag = displayFlag;
    }
    /**
     * Gets the display flag.
        * @return The display flag
        */
    @Override
    public boolean getDisplayFlag() {
        return displayFlag;
    }

    /**
     * Starts a new game. Reinitialize the model's state.
     * @post The model's state is reinitialized
     */
    @Override
    public void startNewGame() {
        initialize();
        setChanged();
        notifyObservers();
    }

    /**
     * Gets the list of colored characters for each attempt.
     * @return The list of colored characters for each attempt
     */
    public List<List<ColoredChar>> getColor() {
        assert coloredChars != null : "ColoredChars should not be null";
        return coloredChars;
    }

    /**
     * Sets the color. Sets the color state of the colored characters based on the current guess and the target equation.
     * @invariant colorState != null
     * @requires currentGuess != null && targetEquation != null
     * @ensures coloredChars.size() <= MAX_ATTEMPTS
     * @pre The current guess and the target equation are not null
     * @post The color state of the colored characters is set based on the current guess and the target equation
     */
    @Override
    public void setColor(){
        assert currentGuess != null : "Current guess should not be null";
        assert targetEquation != null : "Target equation should not be null";

        char[] inputChars = currentGuess.toString().toCharArray();
        char[] targetChars = targetEquation.toCharArray();
        List<ColoredChar> currentAttempt = new ArrayList<>();

        for (int i = 0; i < inputChars.length; i++) {
            if (targetEquation.indexOf(inputChars[i]) == -1) { // If the character is not in the target equation
                colorState = grayState;
            } else if (i < targetEquation.length() && targetChars[i] == inputChars[i]) { // If the character is in the target equation and in the correct position
                colorState = greenState;
            } else { // If the character is in the target equation but in the wrong position
                colorState = orangeState;
            }
            currentAttempt.add(new ColoredChar(inputChars[i], colorState));
        }
        coloredChars.add(currentAttempt);
        setChanged();
        notifyObservers();

        assert coloredChars.size() <= MAX_ATTEMPTS : "Size of coloredChars should not exceed MAX_ATTEMPTS";
    }


    /**
     * Compares the input with the target equation and returns a string with the characters colored based on the comparison.
        * @invariant input != null && target != null
        * @requires input != null && target != null
        * @ensures \result.length() > 0
        * @param input The input to be compared
        * @param target The target equation
        * @pre The input and the target equation are not null
        * @post The result is a string with the characters colored based on the comparison
        * @return A string with the characters colored based on the comparison
     */
    public String compareCLI(String input, String target) {
        assert input != null : "Input should not be null";
        assert target != null : "Target should not be null";

        StringBuilder result = new StringBuilder();
        // For CLI Color display
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_GRAY = "\u001B[90m";

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == target.charAt(i)) {
//                result.append(ANSI_GREEN + input.charAt(i) + ANSI_RESET);
                result.append("Included and in the right place: " + ANSI_GREEN + input.charAt(i) + ANSI_RESET + "\n");
            } else if (target.indexOf(input.charAt(i)) == -1) {
//                result.append(ANSI_GRAY + input.charAt(i) + ANSI_RESET);
                result.append("Not included: " + ANSI_GRAY + input.charAt(i) + ANSI_RESET + "\n");
            } else {
//                result.append(ANSI_YELLOW + input.charAt(i) + ANSI_RESET);
                result.append("Included but not in the correct location: " + ANSI_YELLOW + input.charAt(i) + ANSI_RESET + "\n");
            }
        }
        assert result.length() > 0 : "Result should not be empty";

        return result.toString();
    }

    /**
     * Plays the game in the Command Line Interface (CLI).
        * @requires equation != null
        * @ensures \result.length() > 0
        * @param equation The input equation
        * @pre The equation is not null
        * @post The result is a string with the characters colored based on the comparison
        * @return A string with the characters colored based on the comparison
     */
    public String CLIPlay(String equation) {
        assert equation != null : "Equation should not be null";
        validFlag = processInput(equation);
        String result = validFlag ? compareCLI(currentGuess.toString(), targetEquation) : "Invalid Input!\n";
        if(validFlag) {
            if(isGameWon()) {
                result += "\nCongratulations, You won!";
            } else if(isGameOver()) {
                result += "\nSorry, You lost!";
            } else {
                result += "\n"+getRemainingAttempts() + " Attempts remaining.";
            }
        }
        assert result.length() > 0 : "Result should not be empty";
        return result;
    }

}
