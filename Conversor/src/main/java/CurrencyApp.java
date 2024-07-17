import com.google.gson.Gson;

import com.google.gson.Gson;

public class CurrencyApp {
    public static void main(String[] args) {
        CurrencyFetcher fetcher = new CurrencyFetcher();
        try {
            String jsonResponse = fetcher.fetchCurrencyData();
            Gson gson = new Gson();
            CurrencyResponse currencyResponse = gson.fromJson(jsonResponse, CurrencyResponse.class);


            if (currencyResponse.getRates() != null) {
                new CurrencyConverterGUI(currencyResponse.getRates());
            } else {
                System.out.println("Las tasas de cambio no están disponibles.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
