package clueGame;

public class ComputerPlayer extends Player {
	
    public ComputerPlayer(String name, String color, int row, int column) {
        super(name, color, row, column);
    }

	@Override
	public boolean isHuman() {
		return false;
	}

}
