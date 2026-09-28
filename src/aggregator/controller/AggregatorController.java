package aggregator.controller;

import aggregator.dto.TransactionDataDto;
import aggregator.service.AggregatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AggregatorController {

    private final AggregatorService aggregatorService;

    public AggregatorController(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @GetMapping("/aggregate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TransactionDataDto>> aggregate(@RequestParam("account") String accountId) {
        return ResponseEntity.ok(aggregatorService.queryRemoteServices(accountId));
    }
}
