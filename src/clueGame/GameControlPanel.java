package clueGame;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GameControlPanel extends JPanel {
	private static Board board;

    private JTextField playerNameBox;
    private JTextField diceRollBox;
    private JTextField currentGuessBox;
    private JTextField guessFeedbackBox;
    private JButton nextTurnButton;

    public GameControlPanel() {
        setLayout(new GridLayout(2, 0));

        //top panel
        JPanel upperSection = new JPanel(new GridLayout(1, 4));

        //player info
        JPanel playerInfoPanel = new JPanel(new GridLayout(2, 0));
        playerInfoPanel.add(new JLabel("Current Player"));
        playerNameBox = new JTextField();
        playerNameBox.setEditable(false);
        playerInfoPanel.add(playerNameBox);
        upperSection.add(playerInfoPanel);

        //dice info
        JPanel dicePanel = new JPanel(new GridLayout(2, 1));
        dicePanel.add(new JLabel("Dice Roll"));
        diceRollBox = new JTextField();
        diceRollBox.setEditable(false);
        dicePanel.add(diceRollBox);
        upperSection.add(dicePanel);

        //buttons
        JButton accusationButton = new JButton("Accuse");
        upperSection.add(accusationButton);
        
        nextTurnButton = new JButton("Next Turn");
        upperSection.add(nextTurnButton);

        //bottom section
        JPanel lowerSection = new JPanel(new GridLayout(1, 2));

        //guess display
        JPanel guessBoxPanel = new JPanel(new BorderLayout());
        guessBoxPanel.setBorder(BorderFactory.createTitledBorder("Player Guess"));
        currentGuessBox = new JTextField();
        currentGuessBox.setEditable(false);
        guessBoxPanel.add(currentGuessBox, BorderLayout.CENTER);
        lowerSection.add(guessBoxPanel);

        //guess feedback
        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBorder(BorderFactory.createTitledBorder("Response"));
        guessFeedbackBox = new JTextField();
        guessFeedbackBox.setEditable(false);
        feedbackPanel.add(guessFeedbackBox, BorderLayout.CENTER);
        lowerSection.add(feedbackPanel);

        //add to main panel
        add(upperSection);
        add(lowerSection);
        

    }
    
    public void addNextButtonListener(ActionListener actionListener) {
        nextTurnButton.addActionListener(actionListener);
    }


    //convert string color to display colors
    private Color getColorFromName(String colorName) {
        switch (colorName.toLowerCase()) {
            case "purple":
                return Color.MAGENTA;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "white":
                return Color.WHITE;
            case "yellow":
                return Color.YELLOW;
            case "red":
                return Color.RED;
            default:
                return Color.LIGHT_GRAY;
        }
    }

    public void updateTurnInfo(Player currentPos, int diceRoll) {
        playerNameBox.setText(currentPos.getName());
        playerNameBox.setBackground(getColorFromName(currentPos.getColor()));
        diceRollBox.setText(Integer.toString(diceRoll));
    }

    public void updateGuess(String guess) {
        currentGuessBox.setText(guess);
    }

    public void updateGuessFeedback(String feedback) {
        guessFeedbackBox.setText(feedback);
    }

    // Test Main Method
    public static void main(String[] args) {
    	setUp();
    	GameControlPanel panel = new GameControlPanel();  
		JFrame frame = new JFrame();  
		frame.setContentPane(panel);
		frame.setSize(750, 180); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

        // test filling in the data
	    panel.updateTurnInfo(new ComputerPlayer("Bruno Fernandez", "purple", 1, 1), 5);
	    panel.updateGuess("I have no guess!");
	    panel.updateGuessFeedback("So you have nothing?");

        ClueCardsPanel miscCards = new ClueCardsPanel();
        panel.addNextButtonListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                Board.getInstance().nextTurn(panel, miscCards);
            }
        });
    
    }
    
    public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("./data/Clue_Layout.csv", "./data/ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
		
		board.dealCards();
		
	}
}