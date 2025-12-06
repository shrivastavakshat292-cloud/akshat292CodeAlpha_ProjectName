import java.io.*;
import java.util.*;

// =======================
// STOCK CLASS
// =======================
class Stock {
    String symbol;
    double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    // Simulate price change
    public void updatePrice() {
        double change = (Math.random() * 4) - 2;   // -2 to +2
        price = Math.round((price + change) * 100) / 100.0;
    }
}


// =======================
// MARKET CLASS
// =======================
class Market {
    HashMap<String, Stock> stocks = new HashMap<>();

    public Market() {
        stocks.put("AAPL", new Stock("AAPL", 180));
        stocks.put("TSLA", new Stock("TSLA", 220));
        stocks.put("GOOG", new Stock("GOOG", 140));
    }

    // Update all stock prices
    public void updateMarket() {
        for (Stock s : stocks.values()) {
            s.updatePrice();
        }
    }

    // Display prices
    public void showPrices() {
        System.out.println("\n📈 Current Market Prices:");
        for (Stock s : stocks.values()) {
            System.out.println(s.symbol + " : $" + s.price);
        }
    }
}


// =======================
// TRANSACTION CLASS
// =======================
class Transaction {
    String type; // BUY or SELL
    String symbol;
    int qty;
    double price;

    public Transaction(String type, String symbol, int qty, double price) {
        this.type = type;
        this.symbol = symbol;
        this.qty = qty;
        this.price = price;
    }

    @Override
    public String toString() {
        return type + "," + symbol + "," + qty + "," + price;
    }
}


// =======================
// USER / PORTFOLIO CLASS
// =======================
class User {
    double cash = 5000;
    HashMap<String, Integer> holdings = new HashMap<>();
    ArrayList<Transaction> history = new ArrayList<>();
    ArrayList<Double> performance = new ArrayList<>();

    // Buy stock
    public void buy(Market m, String symbol, int qty) {
        if (!m.stocks.containsKey(symbol)) {
            System.out.println("❌ Invalid stock symbol");
            return;
        }

        Stock s = m.stocks.get(symbol);
        double cost = s.price * qty;

        if (cost > cash) {
            System.out.println("❌ Not enough cash");
            return;
        }

        cash -= cost;
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
        history.add(new Transaction("BUY", symbol, qty, s.price));
        System.out.println("✅ Bought " + qty + " shares of " + symbol);
    }

    // Sell stock
    public void sell(Market m, String symbol, int qty) {
        if (!holdings.containsKey(symbol) || holdings.get(symbol) < qty) {
            System.out.println("❌ Not enough shares");
            return;
        }

        Stock s = m.stocks.get(symbol);
        double revenue = s.price * qty;

        cash += revenue;
        holdings.put(symbol, holdings.get(symbol) - qty);
        if (holdings.get(symbol) == 0) holdings.remove(symbol);
        history.add(new Transaction("SELL", symbol, qty, s.price));

        System.out.println("✅ Sold " + qty + " shares of " + symbol);
    }

    // Calculate total portfolio value
    public double totalValue(Market m) {
        double total = cash;
        for (String sym : holdings.keySet()) {
            total += holdings.get(sym) * m.stocks.get(sym).price;
        }
        return total;
    }

    // Record performance each cycle
    public void recordPerformance(Market m) {
        performance.add(totalValue(m));
    }

    // Save portfolio & transactions
    public void saveData() {
        try {
            PrintWriter p = new PrintWriter(new FileWriter("portfolio.txt"));
            p.println("Cash=" + cash);
            for (String s : holdings.keySet()) {
                p.println(s + "=" + holdings.get(s));
            }
            p.close();

            PrintWriter t = new PrintWriter(new FileWriter("transactions.txt"));
            for (Transaction tr : history) {
                t.println(tr);
            }
            t.close();

            System.out.println("💾 Data saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show portfolio
    public void showPortfolio(Market m) {
        System.out.println("\n📊 Portfolio:");
        for (String s : holdings.keySet()) {
            int q = holdings.get(s);
            double v = q * m.stocks.get(s).price;
            System.out.println(s + " : " + q + " shares  | value $" + v);
        }
        System.out.println("💰 Cash: $" + cash);
        System.out.println("📈 Total Value: $" + totalValue(m));
    }
}


// =======================
// MAIN SYSTEM
//Task-3-StockTradingSystem// =======================
public class StockTradingSystem {
    public static void main(String[] args) {

        Market market = new Market();
        User user = new User();

        for (int i = 0; i < 5; i++) {
            System.out.println("\n=== MARKET CYCLE " + (i + 1) + " ===");

            market.updateMarket();
            market.showPrices();

            if (i == 1) user.buy(market, "AAPL", 5);
            if (i == 3) user.sell(market, "AAPL", 2);

            user.recordPerformance(market);
        }

        user.showPortfolio(market);
        user.saveData();
    }
}
