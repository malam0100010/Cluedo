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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Solution)) return false;
        Solution potentialSolution = (Solution) object;
               
        return Objects.equals(room,   potentialSolution.room) && Objects.equals(person, potentialSolution.person) && Objects.equals(weapon, potentialSolution.weapon);
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
