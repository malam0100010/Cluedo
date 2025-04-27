/*
 * Class: This is the suggestion dialog which is responsible for providing players with feedback on their suggestions
 * Authors: Musad Alam and Valor Buck
 * Date: 2/25/2025
 * Collaborators: Received help from Jack Brennan, Xandier Fermier, and Ivan Lopez-Rubio
 * Sources: StackOverflow, W3 Schools, ChatGPT
 */
package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SuggestionDialog extends JDialog {
    private JComboBox<String> personOptionBox;
    private JComboBox<String> weaponOptionBox;
    private Solution result = null;

    public SuggestionDialog(Room currentRoom, JFrame parent) {
        super(parent, "Make a Suggestion", true);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Room:"));
        add(new JLabel(currentRoom.getName()));

        add(new JLabel("Person:"));
        personOptionBox = new JComboBox<>();
        for (Card card : Board.getInstance().getCards()) {
            if (card.getType() == CardType.PLAYER) {
                personOptionBox.addItem(card.getName());
            }
        }
        add(personOptionBox);

        add(new JLabel("Weapon:"));
        weaponOptionBox = new JComboBox<>();
        for (Card card : Board.getInstance().getCards()) {
            if (card.getType() == CardType.WEAPON) {
                weaponOptionBox.addItem(card.getName());
            }
        }
        add(weaponOptionBox);

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Card roomCard   = new Card(currentRoom.getName(), CardType.ROOM);
                Card personCard = new Card((String)personOptionBox.getSelectedItem(), CardType.PLAYER);
                Card weaponCard = new Card((String)weaponOptionBox.getSelectedItem(), CardType.WEAPON);
                result = new Solution(roomCard, personCard, weaponCard);
                setVisible(false);
            }
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = null;
                setVisible(false);
            }
        });

        add(okBtn);
        add(cancelBtn);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public Solution getResult() {
        return result;
    }
}
