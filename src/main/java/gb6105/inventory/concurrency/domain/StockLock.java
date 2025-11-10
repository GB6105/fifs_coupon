package gb6105.inventory.concurrency.domain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockLock {
    private long id;
    private String name;
    private int quantity;
    private Lock lock = new ReentrantLock();


    public StockLock(long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increase() {
        lock.lock();
        quantity = quantity + 1;
        lock.unlock();
    }

    public void decrease(int quantity) {
        checkQuantity();
        this.quantity -= quantity;
    }

    private void checkQuantity() {
        if (quantity < 0) {
            throw new IllegalArgumentException("재고는 0 미만이 될 수 없습니다.");
        }
    }

}
