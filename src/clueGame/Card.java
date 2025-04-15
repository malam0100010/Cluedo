package clueGame;

public class Card {
	private final String cardName;
	private final CardType cardType;
	
	public Card(String cardName, CardType cardType) {
        this.cardName = cardName;
        this.cardType = cardType;
    }
	
	public boolean equals(Card target) {
		boolean bool = false;
		return bool;
	}
	
	public String getName() {
        return cardName;
    }

    public CardType getType() {
        return cardType;
    }
}
