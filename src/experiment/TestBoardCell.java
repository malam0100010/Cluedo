package experiment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestBoardCell {
	private int row;
	private int col;
	private Set<TestBoardCell> adjList;
	private boolean isRoom = false;
	private boolean isOccupied = false;
	

	public TestBoardCell(int row, int col, boolean isRoom, boolean isOccupied) {
		this.row = row;
		this.col = col;
		this.isRoom = isRoom;
		this.isOccupied = isOccupied;
		adjList = new HashSet<>();
	}
	
	//setter to add a cell to this cells adjacency list
	void addAdjacency(TestBoardCell cell) {
		adjList.add(cell);
	}

	public Set<TestBoardCell> getAdjList() {
		return adjList;
		
	}

	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	public boolean isRoom() {
		return isRoom;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	boolean getOccupied() {
		return isOccupied;
	}
	
	
}
