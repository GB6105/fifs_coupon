package gb6105.inventory.concurrency.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class StockAtomic {
    private long id;
    private String name;
    private AtomicInteger quantity = new AtomicInteger(0);


    public StockAtomic(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void increase() {
        quantity.incrementAndGet();
    }

    public void decrease() {
        checkQuantity();
        quantity.decrementAndGet();
    }

    private void checkQuantity() {
        if (quantity.get() < 0) {
            throw new IllegalArgumentException("재고는 0 미만이 될 수 없습니다.");
        }
    }

}
