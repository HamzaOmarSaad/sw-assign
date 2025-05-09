import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This class represents a GUI for editing and removing assets from a user's asset list.
 * It allows users to update and delete existing assets.
 */
public class Asset_Edit extends JFrame {
    private String username;

    /**
     * This class represents an asset with an ID, type, name, value, and purchase date.
     */
    static class Asset {
        int id;
        String type, name, purchaseDate;
        double value;

        /**
         * Constructs an Asset with the given attributes.
         *
         * @param id the unique identifier of the asset.
         * @param type the type of the asset (e.g., "Stocks").
         * @param name the name of the asset.
         * @param value the value of the asset.
         * @param purchaseDate the purchase date of the asset.
         */
        public Asset(int id, String type, String name, double value, String purchaseDate) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.value = value;
            this.purchaseDate = purchaseDate;
        }

        /**
         * Converts a CSV string into an Asset object.
         *
         * @param line the CSV string containing asset details.
         * @return the Asset object created from the string, or null if invalid.
         */
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

        /**
         * Converts the Asset object to a CSV string representation.
         *
         * @return the CSV string representation of the asset.
         */
        public String toFileString() {
            return id + "," + type + "," + name + "," + value + "," + purchaseDate;
        }

        /**
         * Returns a string representation of the asset for displaying in the UI.
         *
         * @return a string representation of the asset.
         */
        @Override
        public String toString() {
            return "#" + id + " - " + type + ": " + name + " | $" + value + " | " + purchaseDate;
        }
    }

    /**
     * This class handles the storage and management of assets, saving and loading them from a file.
     */
    static class AssetStore {
        private ArrayList<Asset> assets = new ArrayList<>();
        private final String fileName;

        /**
         * Constructs an AssetStore with a file specific to the user's assets.
         *
         * @param username the username of the current user.
         */
        public AssetStore(String username) {
            this.fileName = "assets_" + username + ".txt";
            loadFromFile();
        }

        /**
         * Returns the list of assets.
         *
         * @return the list of assets.
         */
        public ArrayList<Asset> getAssets() {
            return assets;
        }

        /**
         * Updates the asset at the specified index.
         *
         * @param index the index of the asset to update.
         * @param newAsset the updated asset.
         */
        public void updateAsset(int index, Asset newAsset) {
            if (index >= 0 && index < assets.size()) {
                assets.set(index, newAsset);
                saveAllToFile();
            }
        }

        /**
         * Removes the asset at the specified index.
         *
         * @param index the index of the asset to remove.
         */
        public void removeAsset(int index) {
            if (index >= 0 && index < assets.size()) {
                assets.remove(index);
                saveAllToFile();
            }
        }

        /**
         * Saves all assets to the file.
         */
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

        /**
         * Loads assets from the file into memory.
         */
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

    /**
     * Constructs the Asset_Edit GUI and sets up event listeners for interacting with the assets.
     *
     * @param username the username of the current user.
     */
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
