import java.util.Scanner;

public class CLIApp {
    public static void main(String[] args) {
        // Getting User Input with Scanner
        Scanner scanner = new Scanner(System.in);

        // Creating a model instance and initialising the game
        NumberleModel model = new NumberleModel();
        model.initialize();

        String equation;
        String result;

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
