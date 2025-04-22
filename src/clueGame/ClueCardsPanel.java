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
	private static Board board;
	
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
	    basePanel.add(buildPlayerSection());
	    basePanel.add(buildRoomSection());
	    basePanel.add(buildWeaponsSection());
	    
	    add(basePanel, BorderLayout.CENTER);
	}
	
	private JPanel buildPlayerSection() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
	    panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));

	    JLabel handLabel = new JLabel("In Hand:");
	    panel.add(handLabel);

	    for (Card card : humanPlayer.getCardsInHand()) {
	        if (card.getType() == CardType.PLAYER) {
	            playerHandBox = new JTextField(card.getName());
	            playerHandBox.setEditable(false);
	            playerHandBox.setBackground(Color.WHITE);
	            panel.add(playerHandBox);
	        }
	    }

	    JLabel seenLabel = new JLabel("Seen:");
	    panel.add(seenLabel);

	    for (Card card : humanPlayer.getSeenCards()) {
	        if (card.getType() == CardType.PLAYER) {
	            JTextField field = new JTextField(card.getName());
	            field.setEditable(false);
	            field.setBackground(getColorFromName("blue"));
	            panel.add(field);
	        }
	    }

	    return panel;
    }
	
	private JPanel buildRoomSection() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
	    panel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));

	    JLabel handLabel = new JLabel("In Hand:");
	    panel.add(handLabel);

	    for (Card card : humanPlayer.getCardsInHand()) {
	        if (card.getType() == CardType.ROOM) {
	            roomsHandBox = new JTextField(card.getName());
	            roomsHandBox.setEditable(false);
	            roomsHandBox.setBackground(Color.WHITE);
	            panel.add(roomsHandBox);
	        }
	    }

	    JLabel seenLabel = new JLabel("Seen:");
	    panel.add(seenLabel);

	    for (Card card : humanPlayer.getSeenCards()) {
	        if (card.getType() == CardType.ROOM) {
	            JTextField field = new JTextField(card.getName());
	            field.setEditable(false);
	            field.setBackground(getColorFromName("red"));
	            panel.add(field);
	        }
	    }

	    return panel;
	}
	
	private JPanel buildWeaponsSection() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
	    panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));

	    JLabel handLabel = new JLabel("In Hand:");
	    panel.add(handLabel);

	    for (Card card : humanPlayer.getCardsInHand()) {
	        if (card.getType() == CardType.WEAPON) {
	            weaponsHandBox = new JTextField(card.getName());
	            weaponsHandBox.setEditable(false);
	            weaponsHandBox.setBackground(Color.WHITE);
	            panel.add(weaponsHandBox);
	        }
	    }

	    JLabel seenLabel = new JLabel("Seen:");
	    panel.add(seenLabel);

	    for (Card card : humanPlayer.getSeenCards()) {
	        if (card.getType() == CardType.WEAPON) {
	            JTextField field = new JTextField(card.getName());
	            field.setEditable(false);
	            field.setBackground(getColorFromName("purple"));
	            panel.add(field);
	        }
	    }

	    return panel;
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
    	setUp();
        // Set up a mock human player
        humanPlayer = new HumanPlayer("Bruno Fernandez", "Red", 1, 1);
        humanPlayer.updateHand(new Card("Poison Gatorade", CardType.WEAPON));
        humanPlayer.updateHand(new Card("Kit Room", CardType.ROOM));
        humanPlayer.updateHand(new Card("Reuben Amorim", CardType.PLAYER));

        humanPlayer.updateSeen(new Card("Offsides Flag", CardType.WEAPON));
        humanPlayer.updateSeen(new Card("Bathroom", CardType.ROOM));
        humanPlayer.updateSeen(new Card("Fred the Red", CardType.PLAYER));
        humanPlayer.updateSeen(new Card("Broken Pint Glass", CardType.WEAPON));
        humanPlayer.updateSeen(new Card("Concession Stand", CardType.ROOM));
        humanPlayer.updateSeen(new Card("Alexander Isak", CardType.PLAYER));

        // Show panel in frame
        ClueCardsPanel panel = new ClueCardsPanel();
        JFrame frame = new JFrame("Clue Cards");
        frame.setContentPane(panel);
        frame.setSize(250, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
