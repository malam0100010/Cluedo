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
	
	private static final Random randCard = new Random();
	
	public Player(String name, String color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.col = col;
        this.cardsInHand = new HashSet<>();
      //      this.seen = new HashSet<>();
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
	    seenCards.add(seenCard);
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
	
	
    public Card disproveSuggestion(Solution suggestedSolution) {
        List<Card> matchesFound = new ArrayList<>();
        
        
        for (Card card : cardsInHand) {
            if (card.equals(suggestedSolution.getPerson()) || card.equals(suggestedSolution.getWeapon()) || card.equals(suggestedSolution.getRoom())) {
                matchesFound.add(card);
            }
        }
        
        if (matchesFound.isEmpty()) {
            return null;
        } else if (matchesFound.size() == 1) {
            return matchesFound.get(0);
        } else {
            return matchesFound.get(randCard.nextInt(matchesFound.size()));
        }
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
	
	public int getRow() { 
		return row; 
	}
	public int getColumn() { 
		return col; 
	}

	
	
	
}
