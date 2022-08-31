package com.mike.lunchvoter.util;

import com.mike.lunchvoter.exception.CustomSecurityException;
import com.mike.lunchvoter.security.SecurityUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;

@UtilityClass
public class SecurityUtil {

    public static Integer getAuthenticatedUserIdOrElseThrow(@NotNull Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof SecurityUserDetails)) {
            throw new CustomSecurityException("Illegal access exception with authentication = " + authentication);
        }

        return ((SecurityUserDetails) authentication.getPrincipal()).getUser().getId();
    }

}
