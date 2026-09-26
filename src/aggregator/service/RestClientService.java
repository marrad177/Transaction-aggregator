package aggregator.service;

import aggregator.dto.TransactionDataDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.concurrent.CompletableFuture;

@Component
public class RestClientService {
   @Retryable(
            retryFor = {
                    HttpServerErrorException.class,
                    NoResourceFoundException.class
            },
            maxAttempts = 5
//            backoff = @Backoff(delay = 1000, multiplier = 1.5)
    )
   public TransactionDataDto[] fetchTransactions(String uri) {
        RestClient restClient = RestClient.create();;
        TransactionDataDto[] results = restClient
                .get()
                .uri(uri)
                .retrieve()
                .body(TransactionDataDto[].class);
        return results;
    }

    //RestTemplate
    public String pingServer() {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8889/ping";
        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                });
        return response.getBody();
    }
}
