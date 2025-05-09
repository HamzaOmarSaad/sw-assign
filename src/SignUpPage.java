import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Represents the Sign Up window for new users.
 * Allows users to create a new account by entering a username and matching passwords.
 */
public class SignUpPage extends JFrame {
    /** Field for entering the new username. */
    private JTextField usernameField;

    /** Field for entering the new password. */
    private JPasswordField passwordField;

    /** Field for confirming the password. */
    private JPasswordField confirmField;

    /**
     * Constructs the SignUpPage window.
     * Initializes the UI components and sets up the layout and event handling.
     */
    public SignUpPage() {
        setTitle("Sign Up");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("New Username:");
        usernameField = new JTextField(20);

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmField = new JPasswordField(20);

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        customizeButton(registerButton);
        customizeButton(backButton);

        // Event handler for registration
        registerButton.addActionListener(e -> handleRegister());

        // Event handler to return to login
        backButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(confirmLabel, gbc);

        gbc.gridx = 1;
        panel.add(confirmField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        gbc.gridy = 4;
        panel.add(backButton, gbc);

        add(panel);
        setVisible(true);
    }

    /**
     * Styles a button with consistent colors and fonts.
     *
     * @param button The JButton to be styled
     */
    private void customizeButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 120, 215), 2, true));
    }

    /**
     * Handles the user registration logic.
     * Validates input, checks password confirmation, and registers the user if possible.
     * Displays appropriate messages based on the outcome.
     */
    private void handleRegister() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        if (UserDatabase.addUser(user, pass)) {
            JOptionPane.showMessageDialog(this, "Account created. You can now login.");
            dispose();
            new LoginPage();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists.");
        }
    }
}
