// NumberleView.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Observer;


public class NumberleView implements Observer {
    private final INumberleModel model;
    private final NumberleController controller;
    private final JFrame frame = new JFrame("Numberle");
    private final JTextField[][] inputTextFields = new JTextField[7][7];
    private final JButton[] numButtons = new JButton[10];
    private final JButton[] operaButtons = new JButton[6];
    private final JButton newButton = new JButton("New");
    private final JLabel attemptsLabel = new JLabel("Attempts remaining: ");
    private final JLabel targetEquationLabel = new JLabel();
    private int currentFieldIndex=0;
    private boolean firstValidGuessMade = false;

    // Constructor
    public NumberleView(INumberleModel model, NumberleController controller) {
        this.controller = controller;
        this.model = model;
        startSetting();
        this.controller.startNewGame();
        ((NumberleModel)this.model).addObserver(this);
        initializeFrame();
        this.controller.setView(this);
        update((NumberleModel)this.model, null);
    }

    public void startSetting(){
        // Create a dialog
        JDialog dialog = new JDialog(frame, "Game Settings", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new FlowLayout());

        // Create checkboxes
        JCheckBox randomFlagCheckbox = new JCheckBox("Random Flag");
        JCheckBox displayFlagCheckbox = new JCheckBox("Display Flag");

        // Create a button that will take the state of the checkboxes and pass them to the model
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            boolean randomFlag = randomFlagCheckbox.isSelected();
            boolean displayFlag = displayFlagCheckbox.isSelected();
            controller.setFlags(randomFlag, displayFlag);
            dialog.dispose();
        });

        // Add the checkboxes and button to the dialog
        dialog.add(randomFlagCheckbox);
        dialog.add(displayFlagCheckbox);
        dialog.add(okButton);

        // Show the dialog
        dialog.setVisible(true);
    }

    /**
        *  Initialize
     */
    public void initializeFrame() {
        currentFieldIndex = 0;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 620);
        frame.setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
        center.add(new JPanel());

        JPanel inputPanel = new JPanel();
        // Set the layout of the input panel
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < 6; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            for (int j = 0; j < 7; j++) {
                JTextField field = new JTextField();
                field.setPreferredSize(new Dimension(65, 65));
//                field.setMinimumSize(new Dimension(40, 40));
                field.setEditable(false);
                rowPanel.add(field);
                inputTextFields[i][j] = field;
                inputTextFields[i][j].setOpaque(true);
                inputTextFields[i][j].setBackground(Color.WHITE);
            }
            inputPanel.add(rowPanel);
        }



        // Add Enter Button
        JButton submitButton = new JButton("Enter");
        Font font1 = new Font("Arial", Font.BOLD, 20);
        submitButton.setFont(font1); // Setting the font

        // Set the size of the Enter button
        submitButton.setPreferredSize(new Dimension(100, 40));
        // Add Enter Button Listener
        submitButton.addActionListener(e -> {
            StringBuilder guess = new StringBuilder();
            for (JTextField tf : inputTextFields[6-controller.getRemainingAttempts()]) {
                guess.append(tf.getText());
            }
            currentFieldIndex = 0;
            if(controller.processInput(guess.toString()))
                controller.setColor();
            else{
                // Prompt window: "Invalid Input!"
                // delete the input
                for (JTextField tf : inputTextFields[6-controller.getRemainingAttempts()])
                    tf.setText("");
                JOptionPane.showMessageDialog(frame, "Invalid Input! Please re-enter!");
            }

        });



        // Add Attempts Remaining Label
        attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
        attemptsLabel.setFont(font1);
        inputPanel.add(attemptsLabel);
        inputPanel.add(targetEquationLabel);
        center.add(inputPanel);
        center.add(new JPanel());
        frame.add(center, BorderLayout.NORTH);

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS));
        keyboardPanel.add(new JPanel());

        JPanel numberPanel = new JPanel();
//        numberPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        numberPanel.setLayout(new GridLayout());

        keyboardPanel.add(numberPanel);



        // Add Number Buttons
        for (int i = 0; i < 10; i++) {
            String curText = Integer.toString(i);
            numButtons[i] = new JButton(curText);
            numButtons[i].setEnabled(true);
//            numButtons[i].setBounds(0,0,40,40);// Setting the button size
            numButtons[i].setFont(font1); // Setting the font
            // Add Number Button Listener
            numButtons[i].addActionListener(e -> {
                checkLength(curText);
            });
            numberPanel.add(numButtons[i]);
        }



        // Add New Game Button
        newButton.setEnabled(firstValidGuessMade); // Set the enabled state of the New Game button
        newButton.setFont(font1); // Setting the font
        newButton.addActionListener((ActionEvent e) -> {
            currentFieldIndex = 0; // Reset to first text box
            controller.startNewGame();
            // Clear the input fields
            for(JTextField[] row : inputTextFields) {
                for(JTextField tf : row) {
                    if(tf != null){
                        tf.setText("");
                        tf.setBackground(Color.WHITE);
                    }
                }
            }
            // Clear the color of the buttons
            for(int i=0; i<10; i++) numButtons[i].setBackground(null);
            for(int j=0; j<6; j++) operaButtons[j].setBackground(null);
        });



        // Add Operation Buttons
        JPanel operationPanel = new JPanel();
//        operationPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        operationPanel.setLayout(new GridLayout());
        String[] operations = {"+", "-", "*", "/", "=", "Del"};  // Add Operation Buttons
        for (int i = 0; i < 6; i++) {
            String curOperation = operations[i];
            operaButtons[i] = new JButton(curOperation);
            operaButtons[i].setFont(font1); // Setting the font
            // Add Operation Button Listener
            operaButtons[i].addActionListener(e -> {
                if (curOperation.equals("Del")) {
                    if (currentFieldIndex > 0) {
                        currentFieldIndex -= 1; // Delete the previous entered character
                        inputTextFields[6 - controller.getRemainingAttempts()][currentFieldIndex].setText("");
                    }
                } else {
                    checkLength(curOperation);
                }
                });
                operationPanel.add(operaButtons[i]);
            }
        operationPanel.add(newButton);
        operationPanel.add(submitButton);

        keyboardPanel.add(numberPanel);
        keyboardPanel.add(operationPanel);

        frame.add(keyboardPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

    }


    // Check the length of the input
    private void checkLength(String curText) {
        // Check if the currentFieldIndex is out of range
        if (currentFieldIndex >= inputTextFields.length) {
            currentFieldIndex = 0; // Reset currentFieldIndex when it reaches the end
            for (JTextField tf : inputTextFields[6-controller.getRemainingAttempts()]) {
                tf.setText("");
            }
            // Prompt window: "You have exceeded the input range. Please re-enter! "
            JOptionPane.showMessageDialog(frame, "You have exceeded the input range. Please re-enter! ");
        }
        else{
            // Set the text of the current text field
            Font font = new Font("Arial", Font.BOLD, 30);
            inputTextFields[6-controller.getRemainingAttempts()][currentFieldIndex].setFont(font); // Setting the font
            inputTextFields[6-controller.getRemainingAttempts()][currentFieldIndex].setHorizontalAlignment(JTextField.CENTER);
            inputTextFields[6-controller.getRemainingAttempts()][currentFieldIndex].setText(curText); // Setting the text from number buttons
            currentFieldIndex++;
        }
    }


    @Override
    public void update(java.util.Observable o, Object arg) {
        // Update the view
        if (controller.getDisplayFlag()) {
            targetEquationLabel.setText("Target Equation: " + controller.getTargetEquation());
        }
        attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
        firstValidGuessMade = controller.getRemainingAttempts() != 6; // Check if the first valid guess has been made
        newButton.setEnabled(firstValidGuessMade);

        // Set the color of the input fields
        List<List<ColoredChar>> attempts = controller.getColor();
        for (int i = 0; i < attempts.size(); i++) {
            List<ColoredChar> attempt = attempts.get(i);
            for (int j = 0; j < attempt.size(); j++) {
                ColoredChar c = attempt.get(j);
                inputTextFields[i][j].setBackground(c.getColor());
                for (int n = 0; n < 10; n++)
                    //Set the color on each button
                    if(numButtons[n].getText().equals(String.valueOf(c.getChar()))) numButtons[n].setBackground(c.getColor());
                for (int m = 0; m < 6; m++)
                    if(operaButtons[m].getText().equals(String.valueOf(c.getChar()))) operaButtons[m].setBackground(c.getColor());
            }
        }

        if(controller.isGameWon())   // Check if the game is won
            JOptionPane.showMessageDialog(frame, "Congratulations! You won!\nYou can start a new game by clicking New button!");
        else if(controller.isGameOver())   // Check if the game is lost
            JOptionPane.showMessageDialog(frame, "Sorry! You lost!\nYou can start a new game by clicking New button!");
    }
}
