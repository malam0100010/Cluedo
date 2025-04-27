package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
    private List<Card> unseenCards;
    private Random randCard = new Random();
    private boolean accusationReady = false;
    private Solution previousSuggestion = null;
    private Set<Character> visitedRooms = new HashSet<>();

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
        Card personSuggestion = unseenPersons.get(randCard.nextInt(unseenPersons.size()));
        Card weaponSuggestion = unseenWeapons.get(randCard.nextInt(unseenWeapons.size()));
        return new Solution(computerRoom, personSuggestion, weaponSuggestion);
    }

    @Override
    public void updateSeen(Card seenCard) {
        super.updateSeen(seenCard);
        unseenCards.remove(seenCard);
    }

    @Override
    public BoardCell selectTarget(BoardCell startCell, Set<BoardCell> targets) {
        Board board = Board.getInstance();
        List<BoardCell> unseenRooms = new ArrayList<>();
        List<BoardCell> newRooms = new ArrayList<>();
        for (BoardCell cell : targets) {
            char initial = 0;
            if (cell.getIsRoom()) {
                initial = cell.getCellInitial();
            } else if (cell.isDoorway()) {
                int r = cell.getRow(), c = cell.getColumn();
                switch (cell.getDoorDirection()) {
                    case UP:    r++; break;
                    case DOWN:  r--; break;
                    case LEFT:  c++; break;
                    case RIGHT: c--; break;
                }
                initial = board.getCell(r, c).getCellInitial();
            }
            if (initial != 0) {
                String roomName = board.getRoom(initial).getName();
                boolean seen = false;
                for (Card card : getSeenCards()) {
                    if (card.getType() == CardType.ROOM && card.getName().equals(roomName)) {
                        seen = true;
                        break;
                    }
                }
                if (!seen) {
                    unseenRooms.add(cell);
                } else if (!visitedRooms.contains(initial)) {
                    newRooms.add(cell);
                }
            }
        }
        if (!unseenRooms.isEmpty()) {
            return pickRandom(unseenRooms);
        } else if (!newRooms.isEmpty()) {
            return pickRandom(newRooms);
        } else {
            return pickRandom(new ArrayList<>(targets));
        }
    }

    public void markVisitedRoom(char roomInitial) {
        visitedRooms.add(roomInitial);
    }

    private BoardCell pickRandom(List<BoardCell> list) {
        return list.get(randCard.nextInt(list.size()));
    }

    public Solution computerCreatedSuggestion(Room currRoom) {
        Card roomCard = new Card(currRoom.getName(), CardType.ROOM);
        List<Card> peopleNotSeen = new ArrayList<>();
        List<Card> weaponsNotSeen = new ArrayList<>();
        for (Card card : unseenCards) {
            if (card.getType() == CardType.PLAYER) {
                peopleNotSeen.add(card);
            }
        }
        if (peopleNotSeen.isEmpty()) {
            for (Card card : Board.getInstance().getCards()) {
                if (card.getType() == CardType.PLAYER) {
                    peopleNotSeen.add(card);
                }
            }
        }
        Card personSuggested = peopleNotSeen.get(randCard.nextInt(peopleNotSeen.size()));
        for (Card card : unseenCards) {
            if (card.getType() == CardType.WEAPON) {
                weaponsNotSeen.add(card);
            }
        }
        if (weaponsNotSeen.isEmpty()) {
            for (Card card : Board.getInstance().getCards()) {
                if (card.getType() == CardType.WEAPON) {
                    weaponsNotSeen.add(card);
                }
            }
        }
        Card weaponSuggested = weaponsNotSeen.get(randCard.nextInt(weaponsNotSeen.size()));
        return new Solution(roomCard, personSuggested, weaponSuggested);
    }

    public boolean getCompPlayerAccusationReady() {
        return canMakeAccusation();
    }

    public void setCompPlayerAccusationReady(boolean readyToAccuse) {
    }

    public Solution getCompPlayerAccusation() {
        return makeAccusation();
    }

    public void setCompPlayerAccusation(Solution compPlayerSuggestion) {
    }

    public boolean canMakeAccusation() {
        int rooms = 0, people = 0, weapons = 0;
        for (Card c : unseenCards) {
            if (c.getType() == CardType.ROOM) rooms++;
            if (c.getType() == CardType.PLAYER) people++;
            if (c.getType() == CardType.WEAPON) weapons++;
        }
        return rooms == 1 && people == 1 && weapons == 1;
    }

    private Solution makeAccusation() {
        Card room = null, person = null, weapon = null;
        for (Card card : unseenCards) {
            if (card.getType() == CardType.ROOM) room = card;
            if (card.getType() == CardType.PLAYER) person = card;
            if (card.getType() == CardType.WEAPON) weapon = card;
        }
        return new Solution(room, person, weapon);
    }
}
