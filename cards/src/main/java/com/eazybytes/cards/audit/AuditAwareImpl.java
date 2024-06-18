package com.eazybytes.cards.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Implement this interface to provide the current auditor i.e. the current user
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    // Get details of user currently logged in trying to perform certain actions
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("ACCOUNT_MS");
    }

}
