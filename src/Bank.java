import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

/**
 * Represents the Bank Account Management window in the application.
 * Allows users to connect their bank accounts, view connected account information,
 * and see their total assets value. The connection state persists between sessions.
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
        loadBankAccount();
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

        // [Rest of the initializeUI method remains unchanged...]
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

        // [Rest of the updateAccountInfo method remains unchanged...]
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
