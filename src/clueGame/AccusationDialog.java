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

        // Initialize dropdowns
        roomBox = createComboBox(CardType.ROOM);
        personBox = createComboBox(CardType.PLAYER);
        weaponBox = createComboBox(CardType.WEAPON);

        add(new JLabel("Room:"));
        add(roomBox);

        add(new JLabel("Person:"));
        add(personBox);

        add(new JLabel("Weapon:"));
        add(weaponBox);

        // Buttons
        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(e -> submitAccusation());
        add(okBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> cancelAccusation());
        add(cancelBtn);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private JComboBox<String> createComboBox(CardType type) {
        JComboBox<String> comboBox = new JComboBox<>();
        for (Card card : Board.getInstance().getCards()) {
            if (card.getType() == type) {
                comboBox.addItem(card.getName());
            }
        }
        return comboBox;
    }

    private void submitAccusation() {
        result = new Solution(
            new Card((String) roomBox.getSelectedItem(), CardType.ROOM),
            new Card((String) personBox.getSelectedItem(), CardType.PLAYER),
            new Card((String) weaponBox.getSelectedItem(), CardType.WEAPON)
        );
        setVisible(false);
    }

    private void cancelAccusation() {
        result = null;
        setVisible(false);
    }

    public Solution getResult() {
        return result;
    }
}