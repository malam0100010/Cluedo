package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell 
{
    int row;
    int col;
    char inital;
    DoorDirection doorDirection;
    boolean roomLabel;
    boolean roomCenter;
    boolean isRoom;
    boolean isOccupied;
    boolean isDoorway;
    char secretPassage;
    char cellInitial;
    public Set<BoardCell> adjList;

    public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.roomLabel = false;
	    this.roomCenter = false;
	    this.isRoom = false;
	    this.isOccupied = false;
		this.isDoorway = false;
		adjList = new HashSet<>();
	}
    
    public int getColumn(){
    	return this.col;
    }
    
    public int getRow(){
    	return this.row;
    }
    
    public boolean isLabel(){
    	return this.roomLabel;
    }
    
    public boolean isRoomCenter() {
    	return this.roomCenter;
    }
    
    public boolean getIsRoom(){
    	return this.isRoom;
    }
    
    
    public boolean getIsOccupied() {
    	return this.isOccupied;
    }
    
    public boolean isDoorway() {
    	return isDoorway;
    }
    
    public void setRoomLabel(boolean roomLabel) {
    	this.roomLabel = roomLabel;
    }
    
    public void setRoomCenter(boolean roomCenter){
    	this.roomCenter = roomCenter;
    }
    
    public void setIsRoom(boolean isRoom){
    	this.isRoom = isRoom;
    }
    
    public void setIsOccupied(boolean isOccupied) {
    	this.isOccupied = isOccupied;
    }
    
    public void setIsDoorway(boolean isDoorway){
    	this.isDoorway = isDoorway;
    }
    
    public void addAdj(BoardCell someCell) {
        adjList.add(someCell);
    }
    
    public Set<BoardCell> getAdjList() {
    	return adjList;
    }

    public void setDoorDirection(DoorDirection doorDirection) {
    	this.doorDirection = doorDirection;
    }

    public DoorDirection getDoorDirection()
    {
        return this.doorDirection;
    } 

    public char getSecretPassage()
    {
        return secretPassage;
    }
    
    public void setSecretPassage(char secretPass) {
    	secretPassage = secretPass;
    }
    
    public char getCellInital() {
    	return cellInitial;
    }

    public void setCellInitial(char roomInitial) {
    	cellInitial = roomInitial;
    }

}
