import java.util.*;

public class MatchingEngine {
    private OrderBook orderBook;

    public MatchingEngine(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void run() {
        while (true) {
            List<Order> bids = orderBook.getBids();
            List<Order> asks = orderBook.getAsks();

            if (bids.isEmpty() || asks.isEmpty()) {
                // No possible trades can be executed
                break;
            }

            Order highestBid = bids.get(0);
            Order lowestAsk = asks.get(0);

            if (highestBid.getPrice() >= lowestAsk.getPrice()) {
                // There is a match

                int tradeQuantity = Math.min(highestBid.getQuantity(), lowestAsk.getQuantity());

                // Remove the matched orders from the LOB
                orderBook.deleteOrder(highestBid.getId());
                orderBook.deleteOrder(lowestAsk.getId());

                if (highestBid.getQuantity() > tradeQuantity) {
                    // Update the remaining quantity of the highest bid
                    Order updatedBid = new Order(highestBid.getId(), highestBid.getPrice(), highestBid.getQuantity() - tradeQuantity, Side.BUY);
                    orderBook.addOrder(updatedBid);
                }

                if (lowestAsk.getQuantity() > tradeQuantity) {
                    // Update the remaining quantity of the lowest ask
                    Order updatedAsk = new Order(lowestAsk.getId(), lowestAsk.getPrice(), lowestAsk.getQuantity() - tradeQuantity, Side.SELL);
                    orderBook.addOrder(updatedAsk);
                }

                // Print the executed trade
                System.out.println("Executed trade: " + tradeQuantity + " at price " + highestBid.getPrice());

            } else {
                // No possible trades can be executed
                break;
            }
        }
    }
}
