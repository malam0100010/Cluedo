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
    char secretPassage;
    Set<BoardCell> adjList;

    public BoardCell(int row, int col, boolean isRoom, boolean isOccupied) {
		this.row = row;
		this.col = col;
		this.isRoom = isRoom;
		this.isOccupied = isOccupied;
		adjList = new HashSet<>();
	}

    public void addAdj(BoardCell adj)
    {
        
    }

    public boolean isDoorway()
    {
        return false;
    }

    public DoorDirection getDoorDirection()
    {
        return DoorDirection.NONE;
    } 
    
    public boolean isLabel()
    {
        return false;
    }

    public boolean isRoomCenter()
    {
        return false;
    }

    public char getSecretPassage()
    {
        return '?';
    }

}
