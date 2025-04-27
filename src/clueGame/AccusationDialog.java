package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AccusationDialog extends JDialog {
    private JComboBox<String> roomBox;
    private JComboBox<String> personBox;
    private JComboBox<String> weaponBox;
    private Solution result = null;

    public AccusationDialog(JFrame parent) {
        super(parent, "Make an Accusation", true);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Room:"));
        roomBox = new JComboBox<>();
        for (Card card : Board.getInstance().getCards()) {
            if (card.getType() == CardType.ROOM) {
                roomBox.addItem(card.getName());
            }
        }
        add(roomBox);

        add(new JLabel("Person:"));
        personBox = new JComboBox<>();
        for (Card card : Board.getInstance().getCards()) {
            if (card.getType() == CardType.PLAYER) {
                personBox.addItem(card.getName());
            }
        }
        add(personBox);

        add(new JLabel("Weapon:"));
        weaponBox = new JComboBox<>();
        for (Card card : Board.getInstance().getCards()) {
            if (card.getType() == CardType.WEAPON) {
                weaponBox.addItem(card.getName());
            }
        }
        add(weaponBox);

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card roomCard   = new Card((String)roomBox.getSelectedItem(), CardType.ROOM);
                Card personCard = new Card((String)personBox.getSelectedItem(), CardType.PLAYER);
                Card weaponCard = new Card((String)weaponBox.getSelectedItem(), CardType.WEAPON);
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
