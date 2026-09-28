package aggregator.service;

import aggregator.dto.TransactionDataDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static reactor.core.publisher.Mono.zip;

@Service
public class AggregatorService {

    private final WebClientService webClientService;

    public AggregatorService(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    @Cacheable("transactions")
    public List<TransactionDataDto> queryRemoteServices(String accountId) {
        List<TransactionDataDto[]> transactionsList = new ArrayList<>();
        String uri1 = "http://localhost:8888/transactions?account=";
        Mono<TransactionDataDto[]> result1 = webClientService.fetchTransactions(uri1 + accountId);
        String uri2 = "http://localhost:8889/transactions?account=";
        Mono<TransactionDataDto[]> result2 = webClientService.fetchTransactions(uri2 + accountId);
        Mono
                .zip(result1, result2)
                .doOnSuccess(transactionsArrays -> {
                    transactionsList.add(transactionsArrays.getT1());
                    transactionsList.add(transactionsArrays.getT2());
                })
                .block();
        return transactionsList.stream()
                        .flatMap(Arrays::stream)
                        .sorted(Comparator.comparing(TransactionDataDto::timestamp))
                        .toList()
                        .reversed();
    }
}
