package clueGame;

public class HumanPlayer extends Player {
	
    public HumanPlayer(String name, String color, int row, int column) {
        super(name, color, row, column);
    }

	@Override
	public boolean isHuman() {
		return true;
	}
}
