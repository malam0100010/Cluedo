package clueGame;

public class Room 
{
    private String name;
    private BoardCell centerCell;
    private BoardCell labelCell;
    private char roomInital;
   
    public Room(String name, char roomInital, BoardCell centerCell, BoardCell labelCell)
    {
        this.name = name;
        this.roomInital = roomInital;
        this.centerCell = centerCell;
        this.labelCell = labelCell;
    }

    public String getName(){
        return name;
    }
    
    public void setLabelCell(BoardCell someCell){
        this.labelCell = someCell;
    }
    
    public void setCenterCell(BoardCell someCell){
        this.centerCell = someCell;
    }
    
    public BoardCell getLabelCell() {
    	return labelCell;
    }
    
    public BoardCell getCenterCell() {
    	return centerCell;
    }
    

    

}
