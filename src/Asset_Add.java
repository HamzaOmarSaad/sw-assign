import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Represents an asset with basic properties like type, name, value, and purchase date.
 */
class Asset {
    static int idCounter = 1; // For generating unique IDs
    int id;
    String type;
    String name;
    double value;
    String purchaseDate;

    /**
     * Constructs a new Asset with an auto-generated ID.
     *
     * @param type         the asset type (e.g., Stock, Real Estate)
     * @param name         the asset name
     * @param value        the asset value
     * @param purchaseDate the date the asset was purchased
     */
    public Asset(String type, String name, double value, String purchaseDate) {
        this.id = idCounter++;
        this.type = type;
        this.name = name;
        this.value = value;
        this.purchaseDate = purchaseDate;
    }

    /**
     * Constructs an Asset with a specific ID (typically loaded from file).
     *
     * @param id           the asset ID
     * @param type         the asset type
     * @param name         the asset name
     * @param value        the asset value
     * @param purchaseDate the date the asset was purchased
     */
    public Asset(int id, String type, String name, double value, String purchaseDate) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.value = value;
        this.purchaseDate = purchaseDate;

        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    /**
     * Returns a string representation of the asset.
     *
     * @return a human-readable asset string
     */
    @Override
    public String toString() {
        return "#" + id + " - " + type + ": " + name + " | $" + value + " | " + purchaseDate;
    }

    /**
     * Converts the asset to a CSV string for saving to file.
     *
     * @return a CSV-formatted string
     */
    public String toFileString() {
        return id + "," + type + "," + name + "," + value + "," + purchaseDate;
    }

    /**
     * Creates an Asset object from a line of file data.
     *
     * @param line the line from the file
     * @return an Asset object or null if the line is invalid
     */
    public static Asset fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            int id = Integer.parseInt(parts[0]);
            String type = parts[1];
            String name = parts[2];
            double value = Double.parseDouble(parts[3]);
            String purchaseDate = parts[4];
            return new Asset(id, type, name, value, purchaseDate);
        }
        return null;
    }
}

/**
 * Manages a list of assets and handles file persistence.
 */
class AssetStore {
    private ArrayList<Asset> assets = new ArrayList<>();
    private final String fileName;

    /**
     * Initializes the asset store with a user-specific file.
     *
     * @param username the username to personalize file name
     */
    public AssetStore(String username) {
        this.fileName = "assets_" + username + ".txt";
        loadFromFile();
    }

    /**
     * Adds an asset to the list and saves to file.
     *
     * @param asset the asset to add
     */
    public void addAsset(Asset asset) {
        assets.add(asset);
        saveToFile();
    }

    /**
     * Gets all stored assets.
     *
     * @return a list of assets
     */
    public ArrayList<Asset> getAssets() {
        return assets;
    }

    /**
     * Saves the asset list to a file.
     */
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Asset asset : assets) {
                writer.println(asset.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads assets from the user file.
     */
    private void loadFromFile() {
        assets.clear();
        Asset.idCounter = 1;
        File file = new File(fileName);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Asset asset = Asset.fromFileString(line);
                if (asset != null) {
                    assets.add(asset);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/**
 * GUI class for adding and displaying user assets.
 */
public class Asset_Add extends JFrame {
    private JFrame frame;
    private JComboBox<String> assetTypeBox;
    private JTextField nameField, valueField, dateField;
    private DefaultListModel<String> assetListModel;
    private AssetStore assetStore;

    /**
     * Constructs the asset addition GUI for a specific user.
     *
     * @param username the current user's username
     */
    public Asset_Add(String username) {
        assetStore = new AssetStore(username);
        frame = new JFrame("Investor Asset Management - User: " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.decode("#f0f0f0"));
        frame.setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(Color.decode("#f0f0f0"));

        JLabel typeLabel = new JLabel("Asset Type:");
        JLabel nameLabel = new JLabel("Asset Name:");
        JLabel valueLabel = new JLabel("Value ($):");
        JLabel dateLabel = new JLabel("Purchase Date:");

        Color labelColor = Color.decode("#333333");
        typeLabel.setForeground(labelColor);
        nameLabel.setForeground(labelColor);
        valueLabel.setForeground(labelColor);
        dateLabel.setForeground(labelColor);

        inputPanel.add(typeLabel);
        assetTypeBox = new JComboBox<>(new String[]{"Stocks", "Real Estate", "Crypto", "Gold"});
        inputPanel.add(assetTypeBox);

        inputPanel.add(nameLabel);
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(valueLabel);
        valueField = new JTextField();
        inputPanel.add(valueField);

        inputPanel.add(dateLabel);
        dateField = new JTextField("YYYY-MM-DD");
        inputPanel.add(dateField);

        JButton addButton = new JButton("Add Asset");
        addButton.setBackground(new Color(0x2196F3));
        addButton.setForeground(Color.WHITE);
        inputPanel.add(addButton);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(0x757575));
        backButton.setForeground(Color.WHITE);
        inputPanel.add(backButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        assetListModel = new DefaultListModel<>();
        JList<String> assetList = new JList<>(assetListModel);
        JScrollPane scrollPane = new JScrollPane(assetList);
        frame.add(scrollPane, BorderLayout.CENTER);

        for (Asset asset : assetStore.getAssets()) {
            assetListModel.addElement(asset.toString());
        }

        addButton.addActionListener(e -> {
            String type = (String) assetTypeBox.getSelectedItem();
            String name = nameField.getText().trim();
            String valueText = valueField.getText().trim();
            String date = dateField.getText().trim();

            if (name.isEmpty() || valueText.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                return;
            }

            try {
                double value = Double.parseDouble(valueText);
                Asset asset = new Asset(type, name, value, date);
                assetStore.addAsset(asset);
                assetListModel.addElement(asset.toString());

                nameField.setText("");
                valueField.setText("");
                dateField.setText("YYYY-MM-DD");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for value.");
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new Dashboard(username); // Replace with real dashboard if needed
        });

        frame.setVisible(true);
    }
}
