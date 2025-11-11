package gb6105.inventory.service;

import gb6105.inventory.repository.StockRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockFacade {
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final RedissonClient redissonClient;

    public void decreaseStockWithRedis(Long id, Long quantity){
        final RLock lock = redissonClient.getLock(String.format("stockId : %d", id));
        long waitTime = 5l;
        long leaseTime = 1;
        try{
            boolean available = lock.tryLock(waitTime,leaseTime, TimeUnit.SECONDS);
            if(!available){
                System.out.println("redisson lock timeout");
                throw new RuntimeException("redisson lock timeout");
            }
            // Stock 조회
            stockService.decreaseStock(id, quantity);

        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }finally{
            lock.unlock();
        }
    }
}
