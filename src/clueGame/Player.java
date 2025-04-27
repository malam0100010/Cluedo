/*
 * Class: This is the abstract Player class which defines the characteristics of all players (computer and human)
 * Authors: Musad Alam and Valor Buck
 * Date: 2/25/2025
 * Collaborators: Received help from Jack Brennan, Xandier Fermier, and Ivan Lopez-Rubio
 * Sources: StackOverflow, W3 Schools, and ChatGPT
 */
package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	private String name;
	private String color;
	private int row, col;
	private Set<Card> cardsInHand;
	private Set<Card> seenCards;
	private boolean movedPlayer = false;
	private boolean playerEliminated = false;
	
	private static final Random randCard = new Random();
	
	public Player(String name, String color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.col = col;
        this.cardsInHand = new HashSet<>();
    }
	
    public BoardCell selectTarget(BoardCell start, Set<BoardCell> targets) {
        return null;
    }
	
	public void updateHand(Card card) {
		cardsInHand.add(card);
	}
	
	public void updateSeen(Card seenCard) {
		if (seenCards == null) {
	        seenCards = new HashSet<>();
	    }
		System.out.println("Human sees card: " + seenCard.getName());
	    seenCards.add(seenCard);
	}
	
    public Card disproveSuggestion(Solution suggestedSolution) {
        List<Card> matchesFound = new ArrayList<>();
        
        
        for (Card card : cardsInHand) {
            if (card.equals(suggestedSolution.getPerson()) || card.equals(suggestedSolution.getWeapon()) || card.equals(suggestedSolution.getRoom())) {
                matchesFound.add(card);
            }
        }
        
        Card cardShown;
        if (matchesFound.isEmpty()) {
            return null;
        } else if (matchesFound.size() == 1) {
            cardShown = matchesFound.get(0);
        } else {
            cardShown = matchesFound.get(randCard.nextInt(matchesFound.size()));
        }
        
        cardShown.setPlayerWhoShowedCard(this);
        return cardShown;
        
    }
	
    @Override
    public boolean equals(Object object) {
        if (this == object) return true; 
        if (object == null) return false;
        
        if (!(object instanceof Player)) return false;

        Player other = (Player) object;
        boolean posCorrect = (row == other.row && col == other.col);
        boolean objectsCorrect = (Objects.equals(name,  other.name) && Objects.equals(color, other.color));
        return posCorrect && objectsCorrect;
    }
    
    public boolean wasPlayerMoved() {
    	return movedPlayer;
    }
    
    public void setPulledMoved(boolean pullBool) {
    	movedPlayer = pullBool;
    }
	
	public Set<Card> getSeenCards() {
	    if (seenCards == null) {
	        seenCards = new HashSet<>();
	    }
	    return seenCards;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getColor() {
		return this.color;
	}
	
	
	public Set<Card> getCardsInHand() {
        return cardsInHand;
    }
	
	//set location
	public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }
	
	public abstract boolean isHuman();
	
	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.color, this.row, this.col);
	}
	
    public boolean getEliminationStatus() {
    	return playerEliminated;
    }
    
    public void setEliminationStatus(boolean eliminatedBool) {
    	playerEliminated = eliminatedBool;
    }
	
	public int getRow() { 
		return row; 
	}
	public int getColumn() { 
		return col; 
	}
}
