package clueGame;

import java.util.Objects;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public Card(String cardName, CardType cardType) {
        this.cardName = cardName;
        this.cardType = cardType;
    }
		
	public String getName() {
        return this.cardName;
    }

    public CardType getType() {
        return this.cardType;
    }
    
    @Override
    public boolean equals(Object miscObj) {
    	Card card = (Card) miscObj;
    	boolean nameBool = Objects.equals(this.cardName, card.cardName);
    	boolean typeBool = (this.cardType == card.cardType);
    	if(nameBool && typeBool) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(this.cardName, this.cardType);
    }
    
    @Override
    public String toString() {
    	return "Card [name=" + this.getName() + ", type=" + this.getType() + "]";
    }
    
}
