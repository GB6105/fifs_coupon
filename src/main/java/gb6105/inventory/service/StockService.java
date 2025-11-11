package gb6105.inventory.service;

import gb6105.inventory.domain.Stock;
import gb6105.inventory.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decreaseStock(Long id, Long quantity){
        // Stock 조회 -> 동시성 문제 발생 포인트
        Stock stock = stockRepository.findById(id).orElseThrow();
        // 재고 감소
        stock.decrease(quantity);
        // 갱신된 값 저장

    }
}
