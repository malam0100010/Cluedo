package clueGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
    private List<Card> unseenCards;
    private Random randCard = new Random();

    public ComputerPlayer(String name, String color, int row, int column) {
        super(name, color, row, column);

        unseenCards = new ArrayList<>(Board.getInstance().getCards());
        unseenCards.removeAll(getCardsInHand());
    }
        

	@Override
	public boolean isHuman() {
		return false;
	}
	
	public Solution createSuggestion(Solution roomOnly) {
	    Card computerRoom = roomOnly.getRoom();

	    List<Card> unseenPersons = new ArrayList<>();
	    List<Card> unseenWeapons = new ArrayList<>();

	    for (Card card : unseenCards) {
	        if (card.getType() == CardType.PLAYER) {
	            unseenPersons.add(card);
	        } else if (card.getType() == CardType.WEAPON) {
	            unseenWeapons.add(card);
	        }
	    }
	    if (unseenPersons.isEmpty()) {
	        for (Card card : Board.getInstance().getCards()) {
	            if (card.getType() == CardType.PLAYER) {
	            	unseenPersons.add(card);
	            }
	        }
	    }
	    if (unseenWeapons.isEmpty()) {
	        for (Card card : Board.getInstance().getCards()) {
	            if (card.getType() == CardType.WEAPON) {
	            	unseenWeapons.add(card);
	            }
	        }
	    }
	    
	    Card personSuggestion  = unseenPersons.get(randCard.nextInt(unseenPersons.size()));
	    Card weaponSuggestion  = unseenWeapons.get(randCard.nextInt(unseenWeapons.size()));

	    return new Solution(computerRoom, personSuggestion, weaponSuggestion);
	}

	
    @Override
    public void updateSeen(Card seenCard) {
        super.updateSeen(seenCard);      
        unseenCards.remove(seenCard);
    }
    
    @Override
    public BoardCell selectTarget(BoardCell startCell, Set<BoardCell> targets) {

        List<BoardCell> unseenRooms = new ArrayList<>();
        Board board = Board.getInstance();

        for (BoardCell cell : targets) {
            String roomName = null;

            if (cell.getIsRoom()) {
                roomName = board.getRoom(cell.getCellInitial()).getName();
            } 
            else if (cell.isDoorway()) {
                int row = cell.getRow(), col = cell.getColumn();
                switch (cell.getDoorDirection()) {
                    case UP:    
                    	++row; 
                    	break;
                    case DOWN:  
                    	--row; 
                    	break;
                    case LEFT:  
                    	++col; 
                    	break;
                    case RIGHT: 
                    	--col; 
                    	break;
                }
                BoardCell insideRoom = board.getCell(row, col);
                roomName = board.getRoom(insideRoom.getCellInitial()).getName();
            }

            if (roomName != null) {
                boolean seenCard = false;
                for (Card card : getSeenCards()) {
                    if (card.getType() == CardType.ROOM &&
                        card.getName().equals(roomName)) {
                        seenCard = true;
                        break;
                    }
                }
                if (!seenCard) unseenRooms.add(cell);
            }
        }

        if (!unseenRooms.isEmpty()) {
            return unseenRooms.get(randCard.nextInt(unseenRooms.size()));
        }

        List<BoardCell> list = new ArrayList<>(targets);
        return list.get(randCard.nextInt(list.size()));
    }


}
