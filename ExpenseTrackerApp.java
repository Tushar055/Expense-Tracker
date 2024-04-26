import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseTrackerApp extends JFrame {
    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<String> categoryComboBox;
    private JTextArea detailsArea;
    private JButton addButton;
    private JTabbedPane tabbedPane;
    private JPanel displayEntriesPanel;
    private List<String> entriesList;

    public ExpenseTrackerApp() {
        setTitle("Expense Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        entriesList = new ArrayList<>();

        initComponents();
        addComponents();
    }

    private void initComponents() {
        amountField = new JTextField(15);
        dateField = new JTextField(15);

        categoryComboBox = new JComboBox<>(new String[]{"Food", "Transportation", "Entertainment", "Others"});
        detailsArea = new JTextArea(5, 20);
        addButton = new JButton("Add Expense");
        tabbedPane = new JTabbedPane();

        // Add a new tab for adding entries
        JPanel addEntryPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        addEntryPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridy++;
        addEntryPanel.add(new JLabel("Date:"), gbc);
        gbc.gridy++;
        addEntryPanel.add(new JLabel("Category:"), gbc);
        gbc.gridy++;
        addEntryPanel.add(new JLabel("Details:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        addEntryPanel.add(amountField, gbc);
        gbc.gridy++;
        addEntryPanel.add(dateField, gbc);
        gbc.gridy++;
        addEntryPanel.add(categoryComboBox, gbc);
        gbc.gridy++;
        addEntryPanel.add(new JScrollPane(detailsArea), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addEntryPanel.add(addButton, gbc);

        tabbedPane.addTab("Add Entry", addEntryPanel);

        // Add a new panel for displaying entries
        displayEntriesPanel = new JPanel(new GridBagLayout());
        tabbedPane.addTab("Display Entries", displayEntriesPanel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amount = amountField.getText();
                String date = dateField.getText();
                String category = (String) categoryComboBox.getSelectedItem();
                String details = detailsArea.getText();

                String entry = "Amount: " + amount + "\nDate: " + date + "\nCategory: " + category + "\nDetails: " + details + "\n";

                entriesList.add(entry);
                updateDisplayEntriesPanel();

                // Reset input fields to default values
                amountField.setText("");
                dateField.setText("");
                categoryComboBox.setSelectedIndex(0);
                detailsArea.setText("");
            }
        });

        // Add tab for currency converter
        JPanel currencyConverterPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        currencyConverterPanel.add(new JLabel("Value:"), gbc);
        gbc.gridy++;
        JTextField valueField = new JTextField(15);
        currencyConverterPanel.add(valueField, gbc);

        // Dropdown for original currency
        String[] currencies = {"USD", "EUR", "JPY", "GBP", "AUD", "INR"};
        gbc.gridy++;
        currencyConverterPanel.add(new JLabel("Original Currency:"), gbc);
        gbc.gridy++;
        JComboBox<String> originalCurrencyComboBox = new JComboBox<>(currencies);
        currencyConverterPanel.add(originalCurrencyComboBox, gbc);

        // Dropdown for target currency
        gbc.gridy++;
        currencyConverterPanel.add(new JLabel("Target Currency:"), gbc);
        gbc.gridy++;
        JComboBox<String> targetCurrencyComboBox = new JComboBox<>(currencies);
        currencyConverterPanel.add(targetCurrencyComboBox, gbc);

        // Button for conversion
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton convertButton = new JButton("Convert");
        currencyConverterPanel.add(convertButton, gbc);

        // Label for result
        gbc.gridy++;
        JLabel resultLabel = new JLabel("Converted Amount:");
        currencyConverterPanel.add(resultLabel, gbc);

        // Action listener for conversion
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected currencies and amount
                String originalCurrency = (String) originalCurrencyComboBox.getSelectedItem();
                String targetCurrency = (String) targetCurrencyComboBox.getSelectedItem();
                double value = Double.parseDouble(valueField.getText());

                // Perform currency conversion
                double result = convertCurrency(value, originalCurrency, targetCurrency);

                // Update the result label
                resultLabel.setText("Converted Amount: " + result + " " + targetCurrency);
            }
        });

        tabbedPane.addTab("Currency Converter", currencyConverterPanel);
    }

    private void addComponents() {
        add(tabbedPane);
    }

    private void updateDisplayEntriesPanel() {
        displayEntriesPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        for (String entry : entriesList) {
            String[] lines = entry.split("\n");
            String details = lines[3].substring(9); // Get the details part after removing "Details: "
            JLabel entryLabel = new JLabel("> " + details);
            entryLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            entryLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            entryLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showEntryDetails(entry);
                }
            });
            displayEntriesPanel.add(entryLabel, gbc);
            gbc.gridy++;
        }
        displayEntriesPanel.revalidate();
        displayEntriesPanel.repaint();
    }

    private void showEntryDetails(String entry) {
        JTextArea detailsArea = new JTextArea(entry, 10, 50);
        detailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the details window
                JOptionPane.getRootFrame().dispose();
            }
        });
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.add(scrollPane, BorderLayout.CENTER);
        detailsPanel.add(closeButton, BorderLayout.SOUTH);
        JOptionPane.showMessageDialog(this, detailsPanel, "Entry Details", JOptionPane.PLAIN_MESSAGE);
    }

    private double convertCurrency(double value, String originalCurrency, String targetCurrency) {
        // Define exchange rates (for demonstration purposes)
        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.82);
        exchangeRates.put("JPY", 110.39);
        exchangeRates.put("GBP", 0.71);
        exchangeRates.put("AUD", 1.29);
        exchangeRates.put("INR", 73.35); // Added INR exchange rate

        // Get the exchange rates for the original and target currencies
        double originalRate = exchangeRates.get(originalCurrency);
        double targetRate = exchangeRates.get(targetCurrency);

        // Perform currency conversion
        return (value / originalRate) * targetRate;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ExpenseTrackerApp().setVisible(true);
            }
        });
    }
}
