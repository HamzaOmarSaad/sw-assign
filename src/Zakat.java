import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class Zakat extends JFrame {
    private String username;

    public Zakat(String username) {
        this.username = username;

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

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel idLabel = new JLabel("Investment ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField idField = new JTextField(15);
        idField.setMaximumSize(new Dimension(200, 30));
        idPanel.add(idLabel);
        idPanel.add(idField);

        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel amountLabel = new JLabel("Amount ($):");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField amountField = new JTextField(15);
        amountField.setMaximumSize(new Dimension(200, 30));
        amountPanel.add(amountLabel);
        amountPanel.add(amountField);

        JTextArea resultArea = new JTextArea(6, 25);
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

        JButton calculateButton = new JButton("Calculate Zakat");
        customizeButton(calculateButton);
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateButton.addActionListener(e -> {
            try {
                int invID = Integer.parseInt(idField.getText());
                double amount = Double.parseDouble(amountField.getText());
                double zakat = amount * 0.025;

                resultArea.setText(String.format(
                        "Zakat Calculation Results:\n\n" +
                                "Investment ID: %d\n" +
                                "Amount: $%,.2f\n" +
                                "Zakat (2.5%%): $%,.2f\n\n" +
                                "Zakat is an obligatory charity in Islam that is " +
                                "required from Muslims who meet the necessary criteria of wealth.",
                        invID, amount, zakat
                ));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numbers for all fields",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back to Dashboard");
        customizeButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            dispose();
            new Dashboard(username);
        });

        formPanel.add(idPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(amountPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(scrollPane);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(calculateButton);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(backButton);

        add(mainPanel);
        setVisible(true);
    }

    private void customizeButton(JButton button) {
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 120, 215), 2, true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Zakat("testUser"));
    }
}
