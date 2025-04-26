/*
 * Class: This is the BoardPanel class which is responsible for where drawing the panel, coloring them, and 
 * ensuring player input is valid.
 * Authors: Musad Alam and Valor Buck
 * Date: 2/25/2025
 * Collaborators: Received help from Jack Brennan, Xandier Fermier, and Ivan Lopez-Rubio
 * Sources: StackOverflow, W3 Schools, and ChatGPT
 */
package clueGame;

import java.awt.*;
import javax.swing.*;

public class BoardPanel extends JPanel {
    private final Board board;

    public BoardPanel(Board board) { 
    	this.board = board;
    	
    	
        addMouseListener(new java.awt.event.MouseAdapter() {
        	@Override
            public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        		if(!board.getHumanTurn())
        		{
        			return;
        		}
        		
            	// Ensure in bounds
                int rows = board.getNumRows();
                int cols = board.getNumColumns();
                int cell = Math.min(getWidth() / cols, getHeight() / rows);

                int col = mouseEvent.getX() / cell;
                int row = mouseEvent.getY() / cell;

                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    BoardCell clickedCell = board.getCell(row, col);
                    board.boardClick(clickedCell);
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int totalRows = board.getNumRows();
        int totalCols = board.getNumColumns();
        int cell = Math.min(getWidth() / totalCols, getHeight() / totalRows);

        for (int numRows = 0; numRows < totalRows; ++numRows) {
            for (int numCols = 0; numCols < totalCols; ++numCols) {
                	board.getCell(numRows, numCols).draw(graphics, numCols * cell, numRows * cell, cell);
            	}
        }
        for (int numRows = 0; numRows < totalRows;  ++numRows) {
            for (int numCols = 0; numCols < totalCols;  ++numCols) {
                BoardCell bc = board.getCell(numRows,numCols);
                if (bc.isLabel()) {
                    graphics.setColor(Color.BLACK);
                    graphics.drawString(board.getRoom(bc).getName(),
                                 numCols * cell + 2, (numRows + 1) * cell - 4);
                }
            }
        }

        for (Player player : board.getPlayers()) {
            int x = player.getColumn()* cell, y = player.getRow() * cell;
            graphics.setColor(colorFrom(player.getColor()));
            graphics.fillOval(x + 2, y + 2, cell - 4, cell - 4);
            graphics.setColor(Color.BLACK);
            graphics.drawOval(x + 2, y + 2, cell - 4, cell - 4);
        }
    }
    



    private Color colorFrom(String colorStr) {
        colorStr = colorStr.toLowerCase();
        if (colorStr.equals("purple")) {
        	return Color.MAGENTA;
        }
        if (colorStr.equals("blue"))   {
        	return Color.BLUE;
        }
        if (colorStr.equals("green"))  {
        	return Color.GREEN;
        }
        if (colorStr.equals("white"))  {
        	return Color.WHITE;
        }
        if (colorStr.equals("yellow")) {
        	return Color.YELLOW;
        }
        if (colorStr.equals("red"))    {
        	return Color.RED;
        }
        return Color.GRAY;
    }
}
