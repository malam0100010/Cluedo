package clueGame;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Player {
	private String name;
	private String color;
	private int row, col;
	private Set<Card> cardsInHand;
	//	private Set<Card> seen;
	
	public Player(String name, String color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.col = col;
        this.cardsInHand = new HashSet<>();
      //      this.seen = new HashSet<>();
    }
	
	public void updateHand(Card card) {
		cardsInHand.add(card);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getColor() {
		return this.color;
	}
	
	//	public BoardCell getStartingLocation(BoardCell startingCell) {
	//		return startingCell;
	//	}
	//	
	//	public int getRow() {
	//		return row;
	//	}
	//	
	//	public int getCol() {
	//		return col;
	//	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; 
        if (o == null) return false;
        
        if (!(o instanceof Player)) return false;

        Player other = (Player) o;
        return row == other.row &&
               col == other.col &&
               Objects.equals(name,  other.name) &&
               Objects.equals(color, other.color);
    }
	
	public Set<Card> getCardsInHand() {
        return this.cardsInHand;
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
	
}
