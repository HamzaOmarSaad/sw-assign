/*
 * Dashboard.java
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class Dashboard extends JFrame {
    private String username;

    public Dashboard(String username) {
        this.username = username;

        setTitle("Dashboard - Welcome " + username);
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

        func1Button.addActionListener(e -> {
            dispose();
            new Asset_Add(username);
        });

        func2Button.addActionListener(e -> {
            dispose();
            new Asset_Edit(username);
        });

        func3Button.addActionListener(e -> {
            dispose();
            new Zakat(username);
        });

        func4Button.addActionListener(e -> {
            dispose();
            new Bank(username);
        });

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
