package clueGame;

import java.util.HashSet;
import java.util.Set;

public abstract class Player {
	private String name;
	private String color;
	private int row, col;
	private Set<Card> cards;
//	private Set<Card> seen;
	
	public Player(String name, String color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.col = col;
      this.cards = new HashSet<>();
//      this.seen = new HashSet<>();
    }
	
	public void updateHand(Card card) {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public BoardCell getStartingLocation(BoardCell startingCell) {
		return startingCell;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public Set<Card> getCards() {
        return cards;
    }
	
	//set location
	public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }
	
	public abstract boolean isHuman();
	
}
