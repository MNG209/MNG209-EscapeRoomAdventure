package game;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class GameGUI extends JFrame {

    private JPanel contentPane;

    private JTextArea sceneDescription;
    private JLabel lblImage;
    private JLabel lblTitle;

    private JButton btnChoice1;
    private JButton btnChoice2;
    private JButton btnPickUpItem;

    private JTextArea txtItemsRoom;
    private JTextArea userInventory;

    private Scene currentScence;
    private AdventureGame game;

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameGUI frame = new GameGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GameGUI() {

        game = new AdventureGame();
        currentScence = game.getCurrentScene();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 550);
        setTitle("Escape Room Adventure");

        contentPane = new JPanel();
        contentPane.setBackground(Color.ORANGE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Room title
        lblTitle = new JLabel("Escape Room Adventure");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(160, 10, 350, 30);
        contentPane.add(lblTitle);

        // Room image
        lblImage = new JLabel("");
        lblImage.setBounds(145, 50, 300, 300);
        contentPane.add(lblImage);

        // Room description
        sceneDescription = new JTextArea();
        sceneDescription.setBounds(170, 365, 300, 45);
        sceneDescription.setLineWrap(true);
        sceneDescription.setWrapStyleWord(true);
        sceneDescription.setEditable(false);
        contentPane.add(sceneDescription);

        // Choice button 1
        btnChoice1 = new JButton("Choice 1");

        btnChoice1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int nextId = currentScence.getChoices().get(0).getNextSceneId();

                game.moveToScene(nextId);
                currentScence = game.getCurrentScene();

                updateSceneDisplay();

                if (currentScence.getSceneId() == 5) {
                    checkWinCondition();
                }
            }
        });

        btnChoice1.setBounds(5, 170, 130, 80);
        contentPane.add(btnChoice1);

        // Choice button 2
        btnChoice2 = new JButton("Choice 2");

        btnChoice2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int nextId = currentScence.getChoices().get(1).getNextSceneId();

                game.moveToScene(nextId);
                currentScence = game.getCurrentScene();

                updateSceneDisplay();

                if (currentScence.getSceneId() == 5) {
                    checkWinCondition();
                }
            }
        });

        btnChoice2.setBounds(450, 170, 130, 80);
        contentPane.add(btnChoice2);

        // Items available in the room
        txtItemsRoom = new JTextArea();
        txtItemsRoom.setBounds(20, 355, 140, 65);
        txtItemsRoom.setLineWrap(true);
        txtItemsRoom.setWrapStyleWord(true);
        txtItemsRoom.setEditable(false);
        contentPane.add(txtItemsRoom);

        // Player inventory
        userInventory = new JTextArea();
        userInventory.setText("Inventory: empty");
        userInventory.setBounds(170, 435, 300, 65);
        userInventory.setLineWrap(true);
        userInventory.setEditable(false);
        contentPane.add(userInventory);

        // Pickup button
        btnPickUpItem = new JButton("Pick up Item");

        btnPickUpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (currentScence.getItem() != null) {

                    game.pickupCurrentItem();

                    updateSceneDisplay();
                }
            }
        });

        btnPickUpItem.setBounds(20, 435, 130, 30);
        contentPane.add(btnPickUpItem);

        // Display starting room
        if (currentScence != null) {
            updateSceneDisplay();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Could not load the starting room. Check data/scenes.csv.");
        }
    }

    public void updateSceneDisplay() {

        // Update room title
        lblTitle.setText(currentScence.getTitle());

        // Update room description
        sceneDescription.setText(currentScence.getDescription());

        // Update items in room
        Item itemInRoom = currentScence.getItem();

        if (itemInRoom == null) {
            txtItemsRoom.setText("Nothing in here");
            btnPickUpItem.setEnabled(false);
        } else {
            txtItemsRoom.setText(itemInRoom.toString());
            btnPickUpItem.setEnabled(true);
        }

        // Update player inventory
        userInventory.setText(game.getPlayer().getInventoryText());

        // Update room image
        ImageIcon icon = new ImageIcon(currentScence.getImagePath());

        Image img = icon.getImage().getScaledInstance(
                300, 300, Image.SCALE_SMOOTH);

        lblImage.setIcon(new ImageIcon(img));

        // Update choice buttons
        btnChoice1.setText("<html>" +
                currentScence.getChoices().get(0).getText() +
                "</html>");

        btnChoice2.setText("<html>" +
                currentScence.getChoices().get(1).getText() +
                "</html>");
    }

    public void checkWinCondition() {

        if (game.canWinGame()) {

            JOptionPane.showMessageDialog(this,
                    "You used the Keycard and the Code Note to unlock the exit.\n"
                    + "You escaped. You win!");

        } else {

            JOptionPane.showMessageDialog(this,
                    "The exit will not open.\n"
                    + "You are missing the required items.\n"
                    + "You need the Keycard and the Code Note.");
        }

        // Stop the game after reaching the exit
        btnChoice1.setEnabled(false);
        btnChoice2.setEnabled(false);
        btnPickUpItem.setEnabled(false);
    }
}