import java.util.Scanner;

public class CLIApp {
    public static void main(String[] args) {
        // Getting User Input with Scanner
        Scanner scanner = new Scanner(System.in);

        // Creating a model instance and initialising the game
        NumberleModel model = new NumberleModel();

        System.out.println("Do you want to enable random flag? (yes/no)");
        String randomFlagInput = scanner.nextLine();
        boolean randomFlag = randomFlagInput.equalsIgnoreCase("yes");
        System.out.println("Do you want to enable display flag? (yes/no)");
        String displayFlagInput = scanner.nextLine();
        boolean displayFlag = displayFlagInput.equalsIgnoreCase("yes");
        model.setFlags(randomFlag, displayFlag);

        model.initialize();

        String equation;
        String result;
        System.out.println("Welcome to Numberle!");
        // game loop
        do {
            System.out.print("Enter equation: ");
            equation = scanner.nextLine();
            result = model.CLIPlay(equation);
            System.out.println(result);
        } while (!model.isGameOver()); // Jump out of the loop at the end of the game

        scanner.close();

    }
}
