/**
 * Name: David Paye
 * Date: July 14, 2025
 * Description: A guessing game using a Java GUI where the user guesses a number between 1 and 1000.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GuessMyNumberGame extends JFrame {
    private JTextField guessField;
    private JLabel feedbackLabel;
    private int targetNumber;
    private int guessCount;
    private long startTime;
    private StringBuilder stats;
    private int round;

    public GuessMyNumberGame() {
        super("Guess My Number");

        setLayout(new FlowLayout()); // basic layout for GUI

        stats = new StringBuilder();
        round = 1;

        JLabel promptLabel = new JLabel("Guess a number between 1 and 1000:");
        add(promptLabel);

        guessField = new JTextField(10);
        add(guessField);

        feedbackLabel = new JLabel(""); // this will show if guess was too high or low
        add(feedbackLabel);

        // I tried using a DocumentListener here first but couldn’t get it to work properly.
        // Could you explain when we should use ActionListener vs DocumentListener for text input?
        guessField.addActionListener(new GuessHandler());

        startNewGame();

        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startNewGame() {
        Random rand = new Random();
        targetNumber = rand.nextInt(1000) + 1;
        guessCount = 0;
        guessField.setEditable(true);
        getContentPane().setBackground(null); // resets the background color
        feedbackLabel.setText("");
        guessField.setText("");
        startTime = System.currentTimeMillis();
    }

    private class GuessHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String guessText = guessField.getText();
            try {
                int guess = Integer.parseInt(guessText);
                guessCount++;

                if (guess < targetNumber) {
                    getContentPane().setBackground(Color.BLUE);
                    feedbackLabel.setText("Too low!");
                } else if (guess > targetNumber) {
                    getContentPane().setBackground(Color.RED);
                    feedbackLabel.setText("Too high!");
                } else {
                    getContentPane().setBackground(Color.GREEN);
                    feedbackLabel.setText("Correct!");
                    guessField.setEditable(false);

                    long endTime = System.currentTimeMillis();
                    long timeTaken = (endTime - startTime) / 1000;

                    stats.append("Round ")
                         .append(round++)
                         .append(": ")
                         .append(guessCount)
                         .append(" guesses, ")
                         .append(timeTaken)
                         .append(" seconds.\n");

                    int playAgain = JOptionPane.showConfirmDialog(
                            null,
                            "You got it in " + guessCount + " guesses!\nPlay again?",
                            "Play Again?",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (playAgain == JOptionPane.YES_OPTION) {
                        startNewGame();
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Game Summary:\n" + stats.toString(),
                                "Summary",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        System.exit(0);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please enter a valid number.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public static void main(String[] args) {
        // I saw some examples online using addActionListener without invokeLater
        // is it always beter to wrap GUI code in invokeLater or is that just for more complex UIs?
        SwingUtilities.invokeLater(() -> new GuessMyNumberGame());
    }
}
