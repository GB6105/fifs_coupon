package gb6105.inventory.concurrency.domain;

public class Stock {
    private long id;
    private String name;
    private int quantity;


    public Stock(long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increase(){
        quantity = quantity + 1;
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
