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
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class BoardPanel extends JPanel {
    private final Board board;
    private final GameControlPanel gameControlPanel;
    private final ClueCardsPanel cardsPanel;

    public BoardPanel(Board board, GameControlPanel gameControlPanel, ClueCardsPanel cardsPanel) { 
    	this.board = board;
    	this.gameControlPanel = gameControlPanel;
    	this.cardsPanel = cardsPanel;
    	
        addMouseListener(new java.awt.event.MouseAdapter() {

        	
        	@Override
            public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
            	HumanPlayer playerHuman = ClueCardsPanel.humanPlayer;
            	if(playerHuman.getEliminationStatus()) {
            		return;
            	}
            	
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
                    
                    if(!board.getHumanTurn() && clickedCell.getIsRoom()) {
                    	HumanPlayer humanPlayer = null;
                    	for(Player player : board.getPlayers()) {
                    		if(player instanceof HumanPlayer) {
                    			humanPlayer = (HumanPlayer) player;
                    			break;
                    		}
                    	}
                    	
                    	System.out.println(">> using cardsPanel = " + cardsPanel);
                    	if(humanPlayer != null) {
                    		JFrame parentFrame = (JFrame)
                    			    SwingUtilities.getWindowAncestor(BoardPanel.this);

                    			SuggestionDialog dialog = new SuggestionDialog(board.getRoom(clickedCell.getCellInitial()),parentFrame);
                    			Solution humanSuggestion = dialog.getResult();
                    		
                    		if(humanSuggestion != null) {
                    			board.processSuggestion(humanPlayer, humanSuggestion, gameControlPanel, cardsPanel);
                    		}
                    	}
                    }
                }
                
                repaint();
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
        if (colorStr == null) {
            return Color.GRAY;
        }
        
        switch (colorStr.toLowerCase()) {
            case "purple": return Color.MAGENTA;
            case "blue": return Color.BLUE;
            case "green": return Color.GREEN;
            case "white": return Color.WHITE;
            case "yellow": return Color.YELLOW;
            case "red": return Color.RED;
            default: return Color.GRAY;
        }
    }

}
