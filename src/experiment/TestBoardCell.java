package experiment;

import java.util.Map;
import java.util.Set;

public class TestBoardCell {
	private int row;
	private int col;
	private Set<TestBoardCell> adjList;
	private boolean isRoom;
	private boolean isOccupied;
	

	TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	//setter to add a cell to this cells adjacency list
	void addAdjacency(TestBoardCell cell) 
	{
		adjList.add(cell);
	}

	Set<TestBoardCell> getAdjList()
	{
		return adjList;
		
	}

	void setRoom(boolean isRoom)
	{
		return;
	}
	
	boolean isRoom()
	{
		return isRoom;
	}

	void setOccupied(boolean isOccupied)
	{
		return;
	}

	boolean getOccupied()
	{
		return isOccupied;
	}
	
	
}
