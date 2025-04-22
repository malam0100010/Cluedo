package clueGame;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.util.*;


public class ClueCardsPanel extends JPanel {
	private static HumanPlayer humanPlayer;
	ArrayList<Card> yourHand;
	ArrayList<Card> seenCards;
	
	private JTextField titleBox;
	private JTextField playerHandBox;
	private JTextField roomsHandBox;
	private JTextField weaponsHandBox;
	private JTextField currentCardBox;
	private JTextField seenCardsBox;
	
	public ClueCardsPanel() {
		setLayout(new BorderLayout());
		
	//base panel
		JPanel basePanel = new JPanel(new GridLayout(3, 1));
	    basePanel.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
	
    //players, rooms, weapons sections
	    basePanel.add(buildCardSection("People", CardType.PLAYER));
	    basePanel.add(buildCardSection("Rooms", CardType.ROOM));
	    basePanel.add(buildCardSection("Weapons", CardType.WEAPON));
	    
	    add(basePanel, BorderLayout.CENTER);
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
    
    //main for testing
    public static void main(String[] args) {
        // Set up a mock human player
        humanPlayer = new HumanPlayer("Pingo", "Red", 1, 2);
        humanPlayer.updateHand(new Card("Dumbbell", CardType.WEAPON));
        humanPlayer.updateHand(new Card("Sauna", CardType.ROOM));
        humanPlayer.updateHand(new Card("Steroid Steven", CardType.PLAYER));

        humanPlayer.updateSeen(new Card("Kettlebell", CardType.WEAPON));
        humanPlayer.updateSeen(new Card("Pool", CardType.ROOM));
        humanPlayer.updateSeen(new Card("Cardio Cathy", CardType.PLAYER));
        humanPlayer.updateSeen(new Card("Jump Rope", CardType.WEAPON));
        humanPlayer.updateSeen(new Card("Lobby", CardType.ROOM));
        humanPlayer.updateSeen(new Card("Hypertrophy Henry", CardType.PLAYER));

        // Show panel in frame
        ClueCardsPanel panel = new ClueCardsPanel();
        JFrame frame = new JFrame("Clue Cards");
        frame.setContentPane(panel);
        frame.setSize(250, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
	
}
