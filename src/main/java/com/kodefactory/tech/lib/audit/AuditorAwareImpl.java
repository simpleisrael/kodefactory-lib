package com.kodefactory.tech.lib.audit;

import com.kodefactory.tech.lib.security.service.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    AuthenticatedUser authenticatedUser;

    @Override
    public String getCurrentAuditor() {
        /*if(authenticatedUser.getAuthenticatedUser() == null) */return "Unauthenticated User";
//        return authenticatedUser.getAuthenticatedUser().getEmail();
    }
}
