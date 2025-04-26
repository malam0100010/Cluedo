package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
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
	private boolean highlightCell = false;

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

	public void draw(Graphics graphics, int rowVal , int colVal, int cellSize) {
		if (cellInitial == 'W')
			graphics.setColor(Color.LIGHT_GRAY);
		else if (cellInitial == 'X')
			graphics.setColor(Color.DARK_GRAY);
		else {
			switch (cellInitial) {
			case 'K':
				graphics.setColor(new Color(97, 182, 119));
				break;
			case 'B':
				graphics.setColor(new Color(92, 64, 51));
				break;
			case 'C':
				graphics.setColor(new Color(255, 255, 75));
				break;
			case 'I':
				graphics.setColor(new Color(203, 195, 227));
				break;
			case 'L':
				graphics.setColor(new Color(239, 231, 219));
				break;
			case 'S':
				graphics.setColor(new Color(212, 161, 42));
				break;
			case 'M':
				graphics.setColor(new Color(218, 41, 28));
				break;
			case 'P':
				graphics.setColor(new Color(173, 216, 230));
				break;
			case 'T':
				graphics.setColor(new Color(78, 87, 102));
				break;
			default:
				graphics.setColor(new Color(97, 182, 119));
				break;
			}
		}

		graphics.fillRect(rowVal , colVal, cellSize, cellSize);
		
        if (highlightCell) {
            graphics.setColor(new Color(212, 175, 55));
            graphics.fillRect(rowVal, colVal, cellSize, cellSize);
        }

		// Outline eacht square
		graphics.setColor(Color.BLACK);
		graphics.drawRect(rowVal , colVal, cellSize, cellSize);

		// Each door is a quarter of the size of a cell and will be on the edge it points to
		if (isDoorway()) {
			graphics.setColor(Color.BLUE);
			int doorWay = cellSize / 4;
			switch (doorDirection) {
			case UP:
				graphics.fillRect(rowVal , colVal, cellSize, doorWay);
				break;
			case DOWN:
				graphics.fillRect(rowVal , colVal + cellSize - doorWay, cellSize, doorWay);
				break;
			case LEFT:
				graphics.fillRect(rowVal , colVal, doorWay, cellSize);
				break;
			case RIGHT:
				graphics.fillRect(rowVal  + cellSize - doorWay, colVal, doorWay, cellSize);
				break;
			}
		}
		
	}
	
	public void highlightCell(boolean highlightBool) {
		// TODO Auto-generated method stub
		highlightCell = highlightBool;
	}

	public int getColumn() {
		return this.col;
	}

	public int getRow() {
		return this.row;
	}

	public boolean isLabel() {
		return this.roomLabel;
	}

	public boolean isRoomCenter() {
		return this.roomCenter;
	}

	public boolean getIsRoom() {
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

	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}

	public void setIsRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public void setDoorway(boolean isDoorway) {
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

	public DoorDirection getDoorDirection() {
		return this.doorDirection;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

	public void setSecretPassage(char secretPass) {
		secretPassage = secretPass;
	}

	public char getCellInitial() {
		return cellInitial;
	}

	public void setCellInitial(char roomInitial) {
		cellInitial = roomInitial;
	}






}
