package clueGame;

import java.util.Objects;

public class Solution {
	private Card room;
	private Card person;
	private Card weapon;
	
    public Solution(Card room, Card person, Card weapon) {
        this.room = room;
        this.person = person;
        this.weapon = weapon;
    }
    
    
    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solution)) return false;
        Solution other = (Solution) o;
        return Objects.equals(room,   other.room) &&
               Objects.equals(person, other.person) &&
               Objects.equals(weapon, other.weapon);
    }
    
    @Override 
    public int hashCode() {
        return Objects.hash(room, person, weapon);
    }
    
    public Card getRoom()   { 
    	return room;  
    }
    
    public Card getPerson() {
    	return person; 
    }
    
    public Card getWeapon() { 
    	return weapon; 
    }
}
