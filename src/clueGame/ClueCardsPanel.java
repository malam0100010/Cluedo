/*
 * Class: This is the ClueCardPanel class which is responsible drawing and interfacing with the game to 
 * keep provided information up to date
 * Authors: Musad Alam and Valor Buck
 * Date: 2/26/2025
 * Collaborators: Received help from Jack Brennan, Xandier Fermier, and Ivan Lopez-Rubio
 * Sources: StackOverflow, W3 Schools, and ChatGPT
 */
package clueGame;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.util.*;


public class ClueCardsPanel extends JPanel {
	static HumanPlayer humanPlayer;
	//list for cards in hand and seen cards
	ArrayList<Card> yourHand;
	ArrayList<Card> seenCards;
	
	private static Board board;
	//texts fields for each section
	private JTextField playerHandBox;
	private JTextField roomsHandBox;
	private JTextField weaponsHandBox;
//	private JTextField currentCardBox;
//	private JTextField seenCardsBox;
	
	public ClueCardsPanel() {
		board = Board.getInstance();
		setPreferredSize(new Dimension(300, getPreferredSize().height));
		setLayout(new BorderLayout());
		
	//base panel
		JPanel basePanel = new JPanel(new GridLayout(3, 1));
	    basePanel.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
	
    //build and add each section
	    basePanel.add(buildPlayerSection());
	    basePanel.add(buildRoomSection());
	    basePanel.add(buildWeaponsSection());
	//add the assembled base panel
	    add(basePanel, BorderLayout.CENTER);
	}
	
	/**
     * Helper method to find the color associated with the player who owns a card.
     * @param card Card to find owner of
     * @return color name as string
     */
    private String ownerColor(Card card) {
        for (Player player : board.getPlayers()) {
            if (player.getCardsInHand().contains(card)) {
                return player.getColor();
            }
        }
        return null;
    }
	
    /**
     * Builds the "People" section of the known cards panel.
     * Displays both "In Hand" and "Seen" cards.
     * @return JPanel containing player cards info
     */
	private JPanel buildPlayerSection() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
	    panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
	    //label for in hand cards
	    JLabel handLabel = new JLabel("In Hand:");
	    panel.add(handLabel);
	    //add each player card that the human player holds
	    for (Card card : humanPlayer.getCardsInHand()) {
	        if (card.getType() == CardType.PLAYER) {
	            playerHandBox = new JTextField(card.getName());
	            playerHandBox.setEditable(false);
	            playerHandBox.setBackground(Color.WHITE);
	            panel.add(playerHandBox);
	        }
	    }
	    //label for seen cards
	    JLabel seenLabel = new JLabel("Seen:");
	    panel.add(seenLabel);
	    //add each player card that the human player has seen
	    for (Card card : humanPlayer.getSeenCards()) {
	        if (card.getType() == CardType.PLAYER) {
	            JTextField field = new JTextField(card.getName());
	            field.setEditable(false);
	            field.setBackground(getColorFromName(ownerColor(card)));
	            panel.add(field);
	        }
	    }

	    return panel;
    }
	
	/**
     * Builds the "Rooms" section of the known cards panel.
     * @return JPanel containing room cards info
     */
	private JPanel buildRoomSection() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
	    panel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));

	    JLabel handLabel = new JLabel("In Hand:");
	    panel.add(handLabel);
	    //display rooms held in hand
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
	    //display rooms seen from other players
	    for (Card card : humanPlayer.getSeenCards()) {
	        if (card.getType() == CardType.ROOM) {
	            JTextField field = new JTextField(card.getName());
	            field.setEditable(false);
	            field.setBackground(getColorFromName(ownerColor(card)));
	            panel.add(field);
	        }
	    }

	    return panel;
	}
	
	/**
     * Builds the "Weapons" section of the known cards panel.
     * @return JPanel containing weapon cards info
     */
	private JPanel buildWeaponsSection() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
	    panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));

	    JLabel handLabel = new JLabel("In Hand:");
	    panel.add(handLabel);
	    //display weapons held in hand
	    for (Card card : humanPlayer.getCardsInHand()) {
	        if (card.getType() == CardType.WEAPON) {
	            weaponsHandBox = new JTextField(card.getName());
	            weaponsHandBox.setEditable(false);
	            weaponsHandBox.setBackground(getColorFromName(ownerColor(card)));
	            panel.add(weaponsHandBox);
	        }
	    }

	    JLabel seenLabel = new JLabel("Seen:");
	    panel.add(seenLabel);
	    //display weapons seen from other players
	    for (Card card : humanPlayer.getSeenCards()) {
	        if (card.getType() == CardType.WEAPON) {
	            JTextField field = new JTextField(card.getName());
	            field.setEditable(false);
	            field.setBackground(getColorFromName(ownerColor(card)));
	            panel.add(field);
	        }
	    }

	    return panel;
	}
	
    public void refresh() {
    	System.out.println(">> ClueCardsPanel.refresh(): seen="
    		    + humanPlayer.getSeenCards().size());
        removeAll();

        JPanel basePanel = new JPanel(new GridLayout(3, 1));
        basePanel.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
        basePanel.add(buildPlayerSection());
        basePanel.add(buildRoomSection());
        basePanel.add(buildWeaponsSection());

        setLayout(new BorderLayout());
        add(basePanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
    
  //convert string color to display colors
    private Color getColorFromName(String colorName) {
    	if (colorName == null) {
    		//default if no other color associated
    		return Color.GRAY;
    	}
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
                return Color.ORANGE;
        }
    }
    	
}

