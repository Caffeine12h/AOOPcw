import java.util.List;

// NumberleController.java
public class NumberleController {
    private INumberleModel model;
    private NumberleView view;

    public NumberleController(INumberleModel model) {
        this.model = model;
    }

    public void setView(NumberleView view) {
        this.view = view;
    }

    public boolean processInput(String input) {
        return model.processInput(input);
    }

    public boolean isGameOver() {
        return model.isGameOver();
    }

    public boolean isGameWon() {
        return model.isGameWon();
    }

    public String getTargetEquation() {
        return model.getTargetEquation();
    }

    public int getRemainingAttempts() {
        return model.getRemainingAttempts();
    }

    public void startNewGame() {
        model.startNewGame();
    }

    public List<List<ColoredChar>> getColor() {
        return model.getColor();
    }

    public void setColor() {model.setColor();}

    public void setFlags(boolean randomFlag, boolean displayFlag) {model.setFlags(randomFlag, displayFlag);}

    public boolean getDisplayFlag() {
        return model.getDisplayFlag();
    }
}
