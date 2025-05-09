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

        // [Rest of the initializeUI method remains unchanged...]
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
