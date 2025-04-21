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
	
    public Solution createSuggestion(Solution possibleSolution) {
        Card computerRoom = possibleSolution.getRoom();
        
        List<Card> seenPersons = new ArrayList<>();
        List<Card> seenWeapons = new ArrayList<>();
        
        for (Card card : unseenCards) {
            if (card.getType() == CardType.PLAYER) {
            	seenPersons.add(card);
            } else if (card.getType() == CardType.WEAPON) {
                seenWeapons.add(card);
            }
        }


        Card personSuggestion = seenPersons.get(randCard.nextInt(seenPersons.size()));
        Card weaponSuggestion = seenWeapons.get(randCard.nextInt(seenWeapons.size()));

        return new Solution(computerRoom, personSuggestion, weaponSuggestion);
    }
	
    @Override
    public void updateSeen(Card seenCard) {
        super.updateSeen(seenCard);      
        unseenCards.remove(seenCard);
    }

	
    public BoardCell selectTarget(Set<BoardCell> targetList) {
        List<BoardCell> unseenRoomDoors = new ArrayList<>();
        Board board = Board.getInstance();
        
        for (BoardCell cell : targetList) {
            if (!cell.isDoorway()) {
            	continue;
            }

            int row = cell.getRow();
            int col = cell.getColumn();
            switch (cell.getDoorDirection()) {
                case UP:    --row; break;
                case DOWN:  ++row; break;
                case LEFT:  --col; break;
                case RIGHT: ++col; break;
            }
            
            BoardCell insideRoom = board.getCell(row, col);
            
            Room someRoom = board.getRoom(insideRoom.getCellInitial());
            
            Card roomCard = new Card(someRoom.getName(), CardType.ROOM);

            if (unseenCards.contains(roomCard)) {
                unseenRoomDoors.add(cell);
            }
        }

        Random randDoor = new Random();
        if (!unseenRoomDoors.isEmpty()) {
            return unseenRoomDoors.get(randDoor.nextInt(unseenRoomDoors.size()));
        }

        List<BoardCell> randTarget = new ArrayList<>(targetList);
        return randTarget.get(randDoor.nextInt(randTarget.size()));
    }
}
