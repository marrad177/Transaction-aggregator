package aggregator.service;

import aggregator.dto.TransactionDataDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AggregatorService {
    private String[] ports = {"8888", "8889"};
    private String uriHead = "http://localhost:";
    private String uriTail = "/transactions?account=";

    private final AsyncProxyService asyncProxyService;

    public AggregatorService(AsyncProxyService asyncProxyService) {
        this.asyncProxyService = asyncProxyService;
    }

    @Cacheable("transactions")
    public List<TransactionDataDto> queryRemoteServices(String accountId) {
        List<TransactionDataDto[]> listResponseLists = new ArrayList<>();

        for (String port : ports) {
            String uri = uriHead + port + uriTail + accountId;
            try {
                CompletableFuture<TransactionDataDto[]> resultList = asyncProxyService.asyncFetchTransactions(uri);
                if (resultList.isDone()) {
                    resultList.
                    listResponseLists.add(resultList.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return listResponseLists.stream()
                .flatMap(array -> Arrays.asList(array).stream())
                .sorted(Comparator.comparing(TransactionDataDto::timestamp))
                .toList()
                .reversed();
    }


}
