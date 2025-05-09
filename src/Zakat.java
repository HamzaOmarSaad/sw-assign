import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Represents the Zakat Calculator window in the application.
 * Allows users to calculate the Zakat (obligatory charity) due on their assets
 * by selecting from their existing portfolio items.
 */
public class Zakat extends JFrame {
    /** The username of the current user. */
    private String username;

    /** Combo box for selecting assets from the user's portfolio. */
    private JComboBox<Asset_Edit.Asset> assetComboBox;

    /** Text area displaying the Zakat calculation results. */
    private JTextArea resultArea;

    /**
     * Constructs a Zakat calculator window for the specified user.
     *
     * @param username the username of the current user
     */
    public Zakat(String username) {
        this.username = username;
        initializeUI();
    }

    /**
     * Initializes and configures the user interface components.
     */
    private void initializeUI() {
        setTitle("Zakat Calculator - " + username);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Title label
        JLabel titleLabel = new JLabel("Zakat Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Load assets from storage
        Asset_Edit.AssetStore assetStore = new Asset_Edit.AssetStore(username);
        ArrayList<Asset_Edit.Asset> assets = assetStore.getAssets();

        // Asset selection panel
        JPanel assetSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel assetLabel = new JLabel("Select Asset:");
        assetLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        assetComboBox = new JComboBox<>();
        for (Asset_Edit.Asset asset : assets) {
            assetComboBox.addItem(asset);
        }
        assetComboBox.setPreferredSize(new Dimension(300, 30));
        assetComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        assetComboBox.setRenderer(new AssetListRenderer());

        assetSelectionPanel.add(assetLabel);
        assetSelectionPanel.add(assetComboBox);

        // Result area
        resultArea = new JTextArea(6, 25);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton calculateButton = new JButton("Calculate Zakat");
        customizeButton(calculateButton);
        calculateButton.addActionListener(e -> calculateZakat());

        JButton backButton = new JButton("Back to Dashboard");
        customizeButton(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new Dashboard(username);
        });

        buttonPanel.add(calculateButton);
        buttonPanel.add(backButton);

        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(assetSelectionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Calculates and displays the Zakat due on the selected asset.
     * Shows a warning if no asset is selected.
     */
    private void calculateZakat() {
        Asset_Edit.Asset selectedAsset = (Asset_Edit.Asset) assetComboBox.getSelectedItem();

        if (selectedAsset == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select an asset first",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double zakat = selectedAsset.value * 0.025;

        resultArea.setText(String.format(
                "Zakat Calculation Results:\n\n" +
                        "Asset: %s\n" +
                        "Type: %s\n" +
                        "Value: $%,.2f\n" +
                        "Purchase Date: %s\n\n" +
                        "Zakat Due (2.5%%): $%,.2f\n\n" +
                        "Zakat is an obligatory charity in Islam that is " +
                        "required from Muslims who meet the necessary criteria of wealth.",
                selectedAsset.name,
                selectedAsset.type,
                selectedAsset.value,
                selectedAsset.purchaseDate,
                zakat
        ));
    }

    /**
     * Custom renderer for displaying assets in the combo box with formatted text.
     */
    private class AssetListRenderer extends DefaultListCellRenderer {
        /**
         * Configures the display of each item in the asset combo box.
         *
         * @param list the JList being rendered
         * @param value the value to render
         * @param index the cell index
         * @param isSelected whether the cell is selected
         * @param cellHasFocus whether the cell has focus
         * @return the configured component for rendering
         */
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Asset_Edit.Asset) {
                Asset_Edit.Asset asset = (Asset_Edit.Asset) value;
                setText(String.format("%s (%s) - $%,.2f", asset.name, asset.type, asset.value));
            }

            return this;
        }
    }

    /**
     * Styles a button with consistent colors and fonts.
     *
     * @param button the button to customize
     */
    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 120, 215), 2, true));
    }

}
