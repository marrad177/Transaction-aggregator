package aggregator.dto;

public record TransactionDataDto(String id,
                                 String serverId,
                                 String account,
                                 String amount,
                                 String timestamp) {
}
