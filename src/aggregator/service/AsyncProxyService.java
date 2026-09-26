package aggregator.service;

import aggregator.dto.TransactionDataDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AsyncProxyService {
    private final RestClientService restClientService;

    public AsyncProxyService(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Async
    public CompletableFuture<TransactionDataDto[]> asyncFetchTransactions(String uri) {
        TransactionDataDto[] resultArray = restClientService.fetchTransactions(uri);
        return CompletableFuture.completedFuture(resultArray);
    }
}
