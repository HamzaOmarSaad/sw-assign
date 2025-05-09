import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

// ===== Asset class =====
class Asset {
    static int idCounter = 1; // For generating unique IDs
    int id;
    String type;
    String name;
    double value;
    String purchaseDate;

    public Asset(String type, String name, double value, String purchaseDate) {
        this.id = idCounter++;
        this.type = type;
        this.name = name;
        this.value = value;
        this.purchaseDate = purchaseDate;
    }

    public Asset(int id, String type, String name, double value, String purchaseDate) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.value = value;
        this.purchaseDate = purchaseDate;

        // Make sure ID counter moves past the highest loaded ID
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    @Override
    public String toString() {
        return "#" + id + " - " + type + ": " + name + " | $" + value + " | " + purchaseDate;
    }

    public String toFileString() {
        return id + "," + type + "," + name + "," + value + "," + purchaseDate;
    }

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

// ===== AssetStore class =====
class AssetStore {
    private ArrayList<Asset> assets = new ArrayList<>();
    private final String fileName;

    public AssetStore(String username) {
        this.fileName = "assets_" + username + ".txt";
        loadFromFile();
    }

    public void addAsset(Asset asset) {
        assets.add(asset);
        saveToFile();
    }

    public ArrayList<Asset> getAssets() {
        return assets;
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Asset asset : assets) {
                writer.println(asset.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
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

// ===== Asset_Add GUI class =====
public class Asset_Add extends JFrame {
    private JFrame frame;
    private JComboBox<String> assetTypeBox;
    private JTextField nameField, valueField, dateField;
    private DefaultListModel<String> assetListModel;
    private AssetStore assetStore;

    public Asset_Add(String username) {
        assetStore = new AssetStore(username);
        frame = new JFrame("Investor Asset Management - User: " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.decode("#f0f0f0"));
        frame.setLocationRelativeTo(null); // Center window

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

        // Load existing assets into list
        for (Asset asset : assetStore.getAssets()) {
            assetListModel.addElement(asset.toString());
        }

        // Add asset action
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

        // Back button (assumes Dashboard exists)
        backButton.addActionListener(e -> {
            frame.dispose();
            new Dashboard(username); // Replace with real dashboard if needed
        });

        frame.setVisible(true);
    }

}
