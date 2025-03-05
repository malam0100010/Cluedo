package clueGame;

public class Room 
{
    String name;
    BoardCell centerCell;
    BoardCell labelCell;

    public Room(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return "";
    }
    
    public BoardCell getLabelCell()
    {
        return new BoardCell(0,0,false,false);
    }

    public BoardCell getCenterCell()
    {
        return new BoardCell(0, 0, false, false);
    }
    

}
