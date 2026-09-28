package aggregator.service;

import aggregator.dto.TransactionDataDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class WebClientService {
    private final WebClient webClient;

    public WebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<TransactionDataDto[]> fetchTransactions(String uri) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(TransactionDataDto[].class)
                .retry(5)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
