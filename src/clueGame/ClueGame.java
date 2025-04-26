 /* Class: This class is responsible for running the clue game and calling necessary functions that control
  * functions such as putting visual components and controlling game flow
 * Authors: Musad Alam and Valor Buck
 * Date: 2/25/2025
 * Collaborators: Received help from Jack Brennan, Xandier Fermier, and Ivan Lopez-Rubio
 * Sources: StackOverflow, W3 Schools, and ChatGPT
 */
package clueGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClueGame extends JFrame {

    private final Board board;
    private final BoardPanel boardPanel;
    private final GameControlPanel controlPanel;
    private final ClueCardsPanel cardsPanel;

    public ClueGame() {
        super("Clue");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 720);
        setLayout(new BorderLayout());

        board = Board.getInstance();
        board.setConfigFiles("./data/Clue_Layout.csv", "./data/ClueSetup.txt");
        board.initialize();
        board.dealCards();

        boardPanel   = new BoardPanel(board);
        board.setBoardPanel(boardPanel);
        controlPanel = new GameControlPanel();
        ClueCardsPanel.humanPlayer = locatePlayer();
        cardsPanel   = new ClueCardsPanel();

        add(boardPanel,   BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(cardsPanel,   BorderLayout.EAST);
        
        controlPanel.addNextButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                board.nextTurn(controlPanel, cardsPanel);
            }
        });

        setVisible(true);
        
        String dialogMsg = "You are "+ locatePlayer().getName() + " (purple).\nCan you find the solution before the Computer Players?";
        JOptionPane.showMessageDialog(this, dialogMsg, "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
        
        board.nextTurn(controlPanel, cardsPanel);
    }
    
	private HumanPlayer locatePlayer() {
        for (Player player : board.getPlayers())
            if (player instanceof HumanPlayer) {
            	return (HumanPlayer) player;
            }
        
        return null;
    }

    public static void main(String[] args) {
        	SwingUtilities.invokeLater(new Runnable() 
        	{
	            public void run() { 
	            	new ClueGame(); 
	            }
        	});
    }
}
