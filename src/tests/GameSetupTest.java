package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import clueGame.*;

class GameSetupTest {
	//constants
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("./data/Clue_Layout.csv", "./data/ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
		
		board.dealCards();
		
	}
	
	@Test
	void testPlayersInDeck() {
		Set<Player> players = board.getPlayers();
		
		assertEquals(6, players.size());

		assertTrue(players.contains(new HumanPlayer("Bruno Fernandez", "Purple", 1, 1)));
		assertTrue(players.contains(new ComputerPlayer("Rueben Amorim", "Blue", 1, 1)));
		assertTrue(players.contains(new ComputerPlayer("Muhammed Salah", "Green", 1, 1)));
		assertTrue(players.contains(new ComputerPlayer("Alexander Isak", "White", 1, 1)));
		assertTrue(players.contains(new ComputerPlayer("Alejandro Garnacho", "Yellow", 1, 1)));
		assertTrue(players.contains(new ComputerPlayer("Fred the Red", "Red", 1, 1)));
		
	}
	
	
	@Test
	void testWeaponsInDeck() {
	    Set<String> weapons = board.getWeapons();
	    
	    assertEquals(6, weapons.size());

	    assertTrue(weapons.contains("Poison Gatorade"));
	    assertTrue(weapons.contains("Offsides Flag"));
	    assertTrue(weapons.contains("Broken Pint Glass"));
	    assertTrue(weapons.contains("Cleat"));
	    assertTrue(weapons.contains("Whistle"));
	    assertTrue(weapons.contains("Clipboard"));
	}
	
   @Test
    public void testPlayerTypes() {
        Set<Player> players = board.getPlayers();

        int humanPlayer = 0;
        int computerPlayers  = 0;
        for (Player player : players) {
            if (((Player) player).isHuman()) {
                ++humanPlayer;
            } else {
                ++computerPlayers;
            }
        }
        
        assertEquals(1, humanPlayer);
        assertEquals(5, computerPlayers);
    }
	
	@Test
	public void testAllCards() {
		Set<Card> cards = board.getCards();
		
		assertEquals(21, cards.size());
		
		assertTrue(cards.contains(new Card("Poison Gatorade",CardType.WEAPON)));
		assertTrue(cards.contains(new Card("Kit Room",CardType.ROOM)));
		assertTrue(cards.contains(new Card("Fred the Red",CardType.PLAYER)));

	}

	
    @Test
    public void testSolution() {
        Solution solution = board.getSolution();

        for (Player player : board.getPlayers()) {
            Set<Card> hand = player.getCardsInHand();

            assertFalse("Room", hand.contains(solution.getRoom()));

            assertFalse("Player", hand.contains(solution.getPerson()));

            assertFalse("Weapon", hand.contains(solution.getWeapon()));
        }
    }
    

    @Test
    public void testEqualCards() {
        Set<Player> players = board.getPlayers();
        
        for (Player player : players) {
            int handSize = player.getCardsInHand().size();
            assertEquals(3, handSize);
        }
        
    }


}
