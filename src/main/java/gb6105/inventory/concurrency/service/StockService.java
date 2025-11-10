package gb6105.inventory.concurrency.service;

import gb6105.inventory.concurrency.domain.Stock;

public class StockService {

    private Stock stock;
    public StockService(Stock stock) {
        this.stock = stock;
    }

    public void decreaseStock(int quantity) {
        stock.decrease(quantity);
    }
}
