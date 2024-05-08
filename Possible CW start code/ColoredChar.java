import java.awt.*;

public class ColoredChar {
    private char character;
    private ColorState colorState;

    public ColoredChar(char character, ColorState colorState) {
        this.character = character;
        this.colorState = colorState;
    }

    public char getChar() {
        return character;
    }

    public Color getColor() {
        return colorState.getColor();
    }

//    public void setColorState(ColorState colorState) {
//        this.colorState = colorState;
//    }
}