package clueGame;

import java.awt.*;
import javax.swing.*;

public class ClueGame extends JFrame {

    private final Board board;
    private final BoardPanel boardPanel;
    private final GameControlPanel controlPanel;
    private final ClueCardsPanel cardsPanel;

    public ClueGame() {
        super("Clue");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setLayout(new BorderLayout());

        board = Board.getInstance();
        board.setConfigFiles("./data/Clue_Layout.csv", "./data/ClueSetup.txt");
        board.initialize();
        board.dealCards();

        boardPanel   = new BoardPanel(board);
        controlPanel = new GameControlPanel();
        ClueCardsPanel.humanPlayer = locatePlayer();
        cardsPanel   = new ClueCardsPanel();

        add(boardPanel,   BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(cardsPanel,   BorderLayout.EAST);

        setVisible(true);
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
