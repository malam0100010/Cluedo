package clueGame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class HumanPlayer extends Player {
	
	
	
    public HumanPlayer(String name, String color, int row, int column) {
        super(name, color, row, column);
    }

	@Override
	public boolean isHuman() {
		return true;
	}
	
    public Solution suggestionDialogPrompt(Room room) {
        return new SuggestionDialog(room, (JFrame) SwingUtilities.getWindowAncestor(null)).getResult();
    }

    public Solution accusationDialogPrompt() {
        return new AccusationDialog((JFrame) SwingUtilities.getWindowAncestor(null)).getResult();
    }
	
}
