import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Asset_Edit extends JFrame {
    private String username;

    // Updated Asset class with ID support
    static class Asset {
        int id;
        String type, name, purchaseDate;
        double value;

        public Asset(int id, String type, String name, double value, String purchaseDate) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.value = value;
            this.purchaseDate = purchaseDate;
        }

        public static Asset fromFileString(String line) {
            String[] parts = line.split(",", 5);
            if (parts.length == 5) {
                int id = Integer.parseInt(parts[0]);
                String type = parts[1];
                String name = parts[2];
                double value = Double.parseDouble(parts[3]);
                String date = parts[4];
                return new Asset(id, type, name, value, date);
            }
            return null;
        }

        public String toFileString() {
            return id + "," + type + "," + name + "," + value + "," + purchaseDate;
        }

        @Override
        public String toString() {
            return "#" + id + " - " + type + ": " + name + " | $" + value + " | " + purchaseDate;
        }
    }

    // AssetStore using user-specific file
    static class AssetStore {
        private ArrayList<Asset> assets = new ArrayList<>();
        private final String fileName;

        public AssetStore(String username) {
            this.fileName = "assets_" + username + ".txt";
            loadFromFile();
        }

        public ArrayList<Asset> getAssets() {
            return assets;
        }

        public void updateAsset(int index, Asset newAsset) {
            if (index >= 0 && index < assets.size()) {
                assets.set(index, newAsset);
                saveAllToFile();
            }
        }

        public void removeAsset(int index) {
            if (index >= 0 && index < assets.size()) {
                assets.remove(index);
                saveAllToFile();
            }
        }

        private void saveAllToFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (Asset asset : assets) {
                    writer.write(asset.toFileString());
                    writer.newLine();
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
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public Asset_Edit(String username) {
        this.username = username;
        setTitle("Edit / Remove Assets - User: " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        AssetStore store = new AssetStore(username);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> assetList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(assetList);
        add(scrollPane, BorderLayout.CENTER);

        for (Asset asset : store.getAssets()) {
            listModel.addElement(asset.toString());
        }

        JButton editBtn = new JButton("Edit Selected");
        JButton removeBtn = new JButton("Remove Selected");
        JButton backBtn = new JButton("Back to Dashboard");

        JPanel btnPanel = new JPanel();
        btnPanel.add(editBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Edit asset
        editBtn.addActionListener(e -> {
            int index = assetList.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Select an asset to edit.");
                return;
            }

            Asset selected = store.getAssets().get(index);

            JTextField nameField = new JTextField(selected.name);
            JTextField valueField = new JTextField(String.valueOf(selected.value));
            JTextField dateField = new JTextField(selected.purchaseDate);
            JComboBox<String> typeBox = new JComboBox<>(new String[]{"Stocks", "Real Estate", "Crypto", "Gold"});
            typeBox.setSelectedItem(selected.type);

            JPanel editPanel = new JPanel(new GridLayout(4, 2));
            editPanel.add(new JLabel("Type:")); editPanel.add(typeBox);
            editPanel.add(new JLabel("Name:")); editPanel.add(nameField);
            editPanel.add(new JLabel("Value:")); editPanel.add(valueField);
            editPanel.add(new JLabel("Purchase Date:")); editPanel.add(dateField);

            int result = JOptionPane.showConfirmDialog(this, editPanel, "Edit Asset", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String type = (String) typeBox.getSelectedItem();
                    String name = nameField.getText().trim();
                    double value = Double.parseDouble(valueField.getText().trim());
                    String date = dateField.getText().trim();

                    Asset updated = new Asset(selected.id, type, name, value, date);
                    store.updateAsset(index, updated);
                    listModel.set(index, updated.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid value format.");
                }
            }
        });

        // Remove asset
        removeBtn.addActionListener(e -> {
            int index = assetList.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Select an asset to remove.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this asset?");
            if (confirm == JOptionPane.YES_OPTION) {
                store.removeAsset(index);
                listModel.remove(index);
            }
        });

        // Back to dashboard
        backBtn.addActionListener(e -> {
            dispose();
            new Dashboard(username); // Use your existing dashboard class
        });

        setVisible(true);
    }
}
