import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Represents the login window for the application.
 * It allows users to enter a username and password to log in,
 * or navigate to the sign-up page.
 */
public class LoginPage extends JFrame {
    /** Text field for entering the username. */
    private JTextField usernameField;

    /** Password field for entering the password. */
    private JPasswordField passwordField;

    /**
     * Constructs a new LoginPage window.
     * Sets up the UI components, layout, and event handlers.
     */
    public LoginPage() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        customizeButton(loginButton);
        customizeButton(signUpButton);

        // Handle login button click
        loginButton.addActionListener(e -> handleLogin());

        // Navigate to the sign-up page
        signUpButton.addActionListener(e -> {
            dispose();
            new SignUpPage();
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
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        gbc.gridy = 3;
        panel.add(signUpButton, gbc);

        add(panel);
        setVisible(true);
    }

    /**
     * Applies a consistent style to buttons.
     *
     * @param button The JButton to style
     */
    private void customizeButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 120, 215), 2, true));
    }

    /**
     * Handles login validation by checking user credentials.
     * If valid, it opens the dashboard. If invalid, it shows an error dialog.
     */
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (UserDatabase.validateUser(user, pass)) {
            dispose();
            new Dashboard(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    }
}
