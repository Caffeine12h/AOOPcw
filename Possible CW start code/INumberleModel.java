import java.io.IOException;
import java.util.List;

public interface INumberleModel {
    int MAX_ATTEMPTS = 6;

    void initialize() throws IOException;
    boolean processInput(String input);
    boolean isGameOver();
    boolean isGameWon();
    String getTargetEquation();
    StringBuilder getCurrentGuess();
    int getRemainingAttempts();
    void startNewGame();

    void setColor();

    List<List<ColoredChar>> getColor();

    void setFlags(boolean randomFlag, boolean displayFlag);

    boolean getDisplayFlag();
}
