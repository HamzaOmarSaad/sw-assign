import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class Zakat extends JFrame {
    private String username;
    private JComboBox<Asset_Edit.Asset> assetComboBox;
    private JTextArea resultArea;

    public Zakat(String username) {
        this.username = username;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Zakat Calculator - " + username);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Zakat Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Asset_Edit.AssetStore assetStore = new Asset_Edit.AssetStore(username);
        ArrayList<Asset_Edit.Asset> assets = assetStore.getAssets();

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

    private class AssetListRenderer extends DefaultListCellRenderer {
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

    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 120, 215), 2, true));
    }

}
