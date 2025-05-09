import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * The Dashboard class represents the main menu of the application
 * after a successful login. It allows users to navigate to different
 * functionalities such as adding/removing assets, zakat calculator,
 * bank account, and logout.
 */
public class Dashboard extends JFrame {
    /** The logged-in user's username. */
    private String username;

    /**
     * Constructs the Dashboard GUI for the specified user.
     *
     * @param username The username of the logged-in user
     */
    public Dashboard(String username) {
        this.username = username;

        setTitle("Dashboard ");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton func1Button = new JButton("Add asset");
        JButton func2Button = new JButton("Remove/Edit Asset");
        JButton func3Button = new JButton("Zakat calculator");
        JButton func4Button = new JButton("Bank account");
        JButton logoutButton = new JButton("Logout");

        customizeButton(func1Button);
        customizeButton(func2Button);
        customizeButton(func3Button);
        customizeButton(func4Button);
        customizeButton(logoutButton);

        // Add Asset
        func1Button.addActionListener(e -> {
            dispose();
            new Asset_Add(username);
        });

        // Remove/Edit Asset
        func2Button.addActionListener(e -> {
            dispose();
            new Asset_Edit(username);
        });

        // Zakat Calculator
        func3Button.addActionListener(e -> {
            dispose();
            new Zakat(username);
        });

        // Bank Account
        func4Button.addActionListener(e -> {
            dispose();
            new Bank(username);
        });

        // Logout
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(func1Button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(func2Button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(func3Button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(func4Button);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }

    /**
     * Applies consistent styling to all buttons used in the dashboard.
     *
     * @param button The JButton to style
     */
    private void customizeButton(JButton button) {
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 120, 215), 2, true));
    }
}
