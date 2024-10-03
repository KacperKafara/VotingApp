package pl.kafara.voting.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;

@Slf4j
@RequiredArgsConstructor
public class TransactionSynchronizationLogger implements TransactionSynchronization {
    private final String txId;



    @Override
    public void afterCompletion(int status) {
        String statusString = switch (status) {
            case STATUS_COMMITTED -> "COMMITTED";
            case STATUS_ROLLED_BACK -> "ROLLED_BACK";
            case STATUS_UNKNOWN -> "UNKNOWN";
            default -> throw new IllegalArgumentException("Unexpected transaction status: " + status);
        };
        log.info("Transaction {} completed with status {}", txId, statusString);
    };
}
