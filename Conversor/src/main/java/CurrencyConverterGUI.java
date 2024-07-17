import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class CurrencyConverterGUI {
    private JTextField amountField;
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JLabel resultLabel;
    private Map<String, Double> rates;

    public CurrencyConverterGUI(Map<String, Double> rates) {
        this.rates = rates;
        initialize();
    }

    private void initialize() {
        JFrame frame = new JFrame("Currency Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        amountField = new JTextField(10);
        fromCurrency = new JComboBox<>(new String[]{"ARS", "BRL", "USD"});
        toCurrency = new JComboBox<>(new String[]{"ARS", "BRL", "USD"});
        JButton convertButton = new JButton("Convert");
        resultLabel = new JLabel("");

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        frame.add(new JLabel("Amount:"));
        frame.add(amountField);
        frame.add(new JLabel("From:"));
        frame.add(fromCurrency);
        frame.add(new JLabel("To:"));
        frame.add(toCurrency);
        frame.add(convertButton);
        frame.add(resultLabel);

        frame.setVisible(true);
    }

    private void convertCurrency() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();

            double convertedAmount = calculateConversion(amount, from, to);
            resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, from, convertedAmount, to));
        } catch (NumberFormatException e) {
            resultLabel.setText("Porfavor Ingresa los datos");
        }
    }

    private double calculateConversion(double amount, String from, String to) {
        double rateFrom = rates.get(from);
        double rateTo = rates.get(to);
        return amount * (rateTo / rateFrom);
    }

    public static void main(String[] args) {

        CurrencyFetcher fetcher = new CurrencyFetcher();
        try {
            String jsonResponse = fetcher.fetchCurrencyData();
            Gson gson = new Gson();
            CurrencyResponse currencyResponse = gson.fromJson(jsonResponse, CurrencyResponse.class);
            new CurrencyConverterGUI(currencyResponse.getRates());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
