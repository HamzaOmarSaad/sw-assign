import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

/**
 * Represents the Bank Integration window in the application.
 * It allows users to connect their bank account using their card details,
 * which then, after connecting, adds the value of their assets to their bank balance
 */

public class Bank extends JFrame {
    /** The username of the current user. */
    private String username;

    /** Combo box for selecting a bank from supported options. */
    private JComboBox<String> bankComboBox;

    /** Field for entering card number. */
    private JTextField cardNumberField;

    /** Field for entering card expiry date. */
    private JTextField expiryDateField;

    /** Field for entering card CVV. */
    private JTextField cvvField;

    /** Field for entering OTP verification code. */
    private JTextField otpField;

    /** Panel containing card details input fields. */
    private JPanel cardDetailsPanel;

    /** Panel containing OTP verification fields. */
    private JPanel otpPanel;

    /** Panel displaying connected account information. */
    private JPanel accountInfoPanel;

    /** Panel containing bank selection options. */
    private JPanel bankSelectionPanel;

    /** Button for connecting/disconnecting bank account. */
    private JButton connectButton;

    /** Flag indicating whether an account is currently connected. */
    private boolean accountConnected = false;

    /** Name of the connected bank. */
    private String connectedBankName;

    /** Last four digits of the connected card. */
    private String connectedCardLastFour;

    /** File name for storing bank account data. */
    private static final String BANK_DATA_FILE = "bank_accounts.dat";

    /** Database of supported banks with their display names and logo references. */
    private final Map<String, String> supportedBanks = new HashMap<>();

    /**
     * Constructs a Bank account management window for the specified user.
     *
     * @param username the username of the current user
     */
    public Bank(String username) {
        this.username = username;
        initializeSupportedBanks();
        loadBankAccount(); // Loads up the already connected bank account, if any
        initializeUI();
    }

    /**
     * Initializes the map of supported banks with their display names and logo references.
     */
    private void initializeSupportedBanks() {
        supportedBanks.put("CIB", "cib.png");
        supportedBanks.put("NBE", "nbe.png");
        supportedBanks.put("Banque Misr", "banquemisr.png");
        supportedBanks.put("QNB", "qnb.png");
        supportedBanks.put("Banque du Caire", "banqueducaire.png");
    }

    /**
     * Handles the case of when user completes the process of connecting his bank account.
     * Loads up the already connected account.
     */
    private void loadBankAccount() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BANK_DATA_FILE))) {
            @SuppressWarnings("unchecked")
            Map<String, String[]> savedAccounts = (Map<String, String[]>) ois.readObject();
            String[] accountData = savedAccounts.get(username);
            if (accountData != null) {
                connectedBankName = accountData[0];
                connectedCardLastFour = accountData[1];
                accountConnected = true;
            }
        } catch (FileNotFoundException e) {
            // No saved accounts yet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the bank account details once the user connects it successfully.
     * Used later in loading the bank account again.
     */
    private void saveBankAccount() {
        try {
            Map<String, String[]> savedAccounts = new HashMap<>();

            // Try to load existing accounts first
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BANK_DATA_FILE))) {
                @SuppressWarnings("unchecked")
                Map<String, String[]> existingAccounts = (Map<String, String[]>) ois.readObject();
                savedAccounts.putAll(existingAccounts);
            } catch (FileNotFoundException e) {
                // File doesn't exist yet, we'll create it
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Add/update current account
            if (accountConnected) {
                savedAccounts.put(username, new String[]{connectedBankName, connectedCardLastFour});
            } else {
                savedAccounts.remove(username);
            }

            // Save all accounts
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BANK_DATA_FILE))) {
                oos.writeObject(savedAccounts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes and configures the user interface components.
     */
    private void initializeUI() {
        setTitle("Bank Account - " + username);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Title label
        JLabel titleLabel = new JLabel("Bank Account Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Account info panel (shown after connection)
        accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new BoxLayout(accountInfoPanel, BoxLayout.Y_AXIS));
        accountInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountInfoPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Bank selection panel
        bankSelectionPanel = new JPanel();
        bankSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bankSelectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bankLabel = new JLabel("Select Your Bank:");
        bankLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        bankComboBox = new JComboBox<>(supportedBanks.keySet().toArray(new String[0]));
        bankComboBox.setPreferredSize(new Dimension(200, 30));
        bankComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        bankSelectionPanel.add(bankLabel);
        bankSelectionPanel.add(bankComboBox);

        // Card details panel
        cardDetailsPanel = new JPanel();
        cardDetailsPanel.setLayout(new BoxLayout(cardDetailsPanel, BoxLayout.Y_AXIS));
        cardDetailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardDetailsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel cardDetailsLabel = new JLabel("Enter Card Details");
        cardDetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cardDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel cardNumberPanel = createFormField("Card Number:", "4242 4242 4242 4242", 20);
        cardNumberField = (JTextField) cardNumberPanel.getComponent(1);

        JPanel expiryDatePanel = createFormField("Expiry Date (MM/YY):", "12/25", 10);
        expiryDateField = (JTextField) expiryDatePanel.getComponent(1);

        JPanel cvvPanel = createFormField("CVV:", "123", 5);
        cvvField = (JTextField) cvvPanel.getComponent(1);

        JButton addCardButton = new JButton("Add Card");
        customizeButton(addCardButton);
        addCardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCardButton.addActionListener(e -> verifyCardDetails());

        cardDetailsPanel.add(cardDetailsLabel);
        cardDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardDetailsPanel.add(cardNumberPanel);
        cardDetailsPanel.add(expiryDatePanel);
        cardDetailsPanel.add(cvvPanel);
        cardDetailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardDetailsPanel.add(addCardButton);

        // OTP panel
        otpPanel = new JPanel();
        otpPanel.setLayout(new BoxLayout(otpPanel, BoxLayout.Y_AXIS));
        otpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        otpPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel otpLabel = new JLabel("Enter OTP Sent to Your Mobile");
        otpLabel.setFont(new Font("Arial", Font.BOLD, 14));
        otpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel otpFieldPanel = createFormField("OTP Code:", "123456", 10);
        otpField = (JTextField) otpFieldPanel.getComponent(1);

        JButton verifyOtpButton = new JButton("Verify OTP");
        customizeButton(verifyOtpButton);
        verifyOtpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        verifyOtpButton.addActionListener(e -> verifyOTP());

        otpPanel.add(otpLabel);
        otpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        otpPanel.add(otpFieldPanel);
        otpPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        otpPanel.add(verifyOtpButton);

        // Navigation buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        connectButton = new JButton(accountConnected ? "Disconnect Bank Account" : "Connect Bank Account");
        customizeButton(connectButton);
        connectButton.addActionListener(e -> {
            if (accountConnected) {
                // Disconnect account
                accountConnected = false;
                connectedBankName = null;
                connectedCardLastFour = null;
                saveBankAccount();
                connectButton.setText("Connect Bank Account");
                showBankSelection();
            } else {
                showCardDetails();
            }
        });

        JButton backButton = new JButton("Back to Dashboard");
        customizeButton(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new Dashboard(username);
        });

        buttonPanel.add(connectButton);
        buttonPanel.add(backButton);

        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(bankSelectionPanel);
        mainPanel.add(cardDetailsPanel);
        mainPanel.add(otpPanel);
        mainPanel.add(accountInfoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);

        // Set initial view based on connection state
        if (accountConnected) {
            showAccountInfo();
        } else {
            showBankSelection();
        }

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Displays the bank selection panel while hiding other panels.
     */
    private void showBankSelection() {
        bankSelectionPanel.setVisible(true);
        cardDetailsPanel.setVisible(false);
        otpPanel.setVisible(false);
        accountInfoPanel.setVisible(false);
    }

    /**
     * Displays the card details input panel after bank selection.
     * Shows a warning if no bank is selected.
     */
    private void showCardDetails() {
        String selectedBank = (String) bankComboBox.getSelectedItem();
        if (selectedBank != null && !selectedBank.isEmpty()) {
            bankSelectionPanel.setVisible(false);
            cardDetailsPanel.setVisible(true);
            otpPanel.setVisible(false);
            accountInfoPanel.setVisible(false);
            pack();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a bank first",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Displays the OTP verification panel while hiding other panels.
     */
    private void showOTPVerification() {
        bankSelectionPanel.setVisible(false);
        cardDetailsPanel.setVisible(false);
        otpPanel.setVisible(true);
        accountInfoPanel.setVisible(false);
    }

    /**
     * Displays the connected account information panel while hiding other panels.
     */
    private void showAccountInfo() {
        updateAccountInfo();
        bankSelectionPanel.setVisible(false);
        cardDetailsPanel.setVisible(false);
        otpPanel.setVisible(false);
        accountInfoPanel.setVisible(true);
        connectButton.setText("Disconnect Bank Account");
    }

    /**
     * Validates the entered card details before proceeding to OTP verification.
     */
    private void verifyCardDetails() {
        String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
        String expiryDate = expiryDateField.getText();
        String cvv = cvvField.getText();

        if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            showError("Please enter a valid 16-digit card number");
            return;
        }

        if (!expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            showError("Please enter expiry date in MM/YY format");
            return;
        }

        if (cvv.length() < 3 || cvv.length() > 4 || !cvv.matches("\\d+")) {
            showError("Please enter a valid CVV (3-4 digits)");
            return;
        }

        // Store the last 4 digits for display
        connectedCardLastFour = cardNumber.substring(12);
        connectedBankName = (String) bankComboBox.getSelectedItem();

        // Show OTP panel
        showOTPVerification();
        pack();
    }

    /**
     * Verifies the entered OTP code and completes the bank account connection process.
     */
    private void verifyOTP() {
        String otp = otpField.getText();

        if (otp.length() != 6 || !otp.matches("\\d+")) {
            showError("Please enter a valid 6-digit OTP");
            return;
        }

        // Mark account as connected
        accountConnected = true;
        saveBankAccount();
        showAccountInfo();

        JOptionPane.showMessageDialog(this,
                "Bank account successfully linked!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        pack();
    }

    /**
     * Updates the account information panel with current connection details and asset values.
     */
    private void updateAccountInfo() {
        accountInfoPanel.removeAll();

        // Calculate total assets value
        double totalAssets = calculateTotalAssets();

        // Connected account info
        JLabel connectedLabel = new JLabel("Connected Bank Account");
        connectedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        connectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bankNameLabel = new JLabel("Bank: " + connectedBankName);
        bankNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel cardLabel = new JLabel("Card: **** **** **** " + connectedCardLastFour);
        cardLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Assets summary
        JLabel assetsTitle = new JLabel("Portfolio Summary");
        assetsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        assetsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel totalAssetsLabel = new JLabel(String.format("Total Assets Value: $%,.2f", totalAssets));
        totalAssetsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel netWorthLabel = new JLabel(String.format("Estimated Net Worth: $%,.2f", totalAssets));
        netWorthLabel.setFont(new Font("Arial", Font.BOLD, 14));

        accountInfoPanel.add(connectedLabel);
        accountInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        accountInfoPanel.add(bankNameLabel);
        accountInfoPanel.add(cardLabel);
        accountInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        accountInfoPanel.add(assetsTitle);
        accountInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        accountInfoPanel.add(totalAssetsLabel);
        accountInfoPanel.add(netWorthLabel);

        accountInfoPanel.revalidate();
        accountInfoPanel.repaint();
    }

    /**
     * Calculates the total value of all assets in the user's portfolio.
     *
     * @return the sum of all asset values
     */
    private double calculateTotalAssets() {
        Asset_Edit.AssetStore assetStore = new Asset_Edit.AssetStore(username);
        double total = 0;
        for (Asset_Edit.Asset asset : assetStore.getAssets()) {
            total += asset.value;
        }
        return total;
    }

    /**
     * Creates a form field panel with label and text input.
     *
     * @param labelText the text for the field label
     * @param placeholder the placeholder text for the input field
     * @param columns the width of the text field in columns
     * @return the configured panel containing the form field
     */
    private JPanel createFormField(String labelText, String placeholder, int columns) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setText(placeholder);

        panel.add(label);
        panel.add(textField);
        return panel;
    }

    /**
     * Displays an error message dialog to the user.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Styles a button with consistent colors and fonts.
     *
     * @param button the button to customize
     */
    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 120, 215), 2, true));
    }

}
