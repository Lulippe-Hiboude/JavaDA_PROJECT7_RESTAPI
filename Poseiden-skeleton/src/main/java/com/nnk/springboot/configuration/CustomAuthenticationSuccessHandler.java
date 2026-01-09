package com.nnk.springboot.configuration;

import com.nnk.springboot.enums.RoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Arrays;

import static com.nnk.springboot.enums.PathEnum.*;
import static com.nnk.springboot.enums.RoleEnum.ROLE_ADMIN;
import static com.nnk.springboot.enums.RoleEnum.ROLE_USER;

/**
 * Custom authentication success handler used by Spring Security.
 * <p>
 * This class is responsible for handling the behavior executed
 * after a successful authentication.
 * It determines the redirection target based on the authenticated
 * user's role.
 *
 * <p>
 * The redirection logic is role-driven and relies on roles declared
 * in {@code RoleEnum}, making the handler easily extensible when new
 * roles are added.
 *
 * <p>
 * If the authenticated user does not have a supported role,
 * they are redirected to a generic error page.
 *
 * <p>
 * This class is used by Spring Security through the
 * {@link AuthenticationSuccessHandler} interface.
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Called by Spring Security when a user has been successfully authenticated.
     * <p>
     * This method delegates the role-based redirection logic
     * to {@link #getRedirectedUrl(Authentication)} and performs
     * the HTTP redirection.
     *
     * @param request        the {@link HttpServletRequest} associated with the authentication
     * @param response       the {@link HttpServletResponse} used to send the redirect
     * @param authentication the {@link Authentication} object containing
     *                       the authenticated user's details and authorities
     * @throws IOException if an input or output error occurs during redirection
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.sendRedirect(getRedirectedUrl(authentication));
    }

    /**
     * Determines the redirection URL based on the authenticated user's role.
     * <p>
     * The method inspects the user's granted authorities and resolves
     * the appropriate redirection path using the application's role definition.
     *
     * <p>
     * If no matching role is found, a default error page is returned.
     *
     * @param authentication the {@link Authentication} object containing
     *                       the user's granted authorities
     * @return the URL to which the user should be redirected
     */
    private String getRedirectedUrl(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(CustomAuthenticationSuccessHandler::isAnExistingRole)
                .findFirst()
                .map(CustomAuthenticationSuccessHandler::getAppropriatePath)
                .orElse(PATH_ERROR.getValue());
    }

    /**
     * Resolves the redirection path associated with a given role.
     * <p>
     * This method maps a valid role to its corresponding
     * application entry point.
     *
     * @param role the role of the authenticated user
     * @return the URL associated with the given role
     */
    private static String getAppropriatePath(String role) {
        return role.equals(ROLE_ADMIN.getValue()) ? PATH_ADMIN_HOME.getValue() : PATH_USER_HOME.getValue();
    }

    /**
     * Checks whether the given role is declared in {@code RoleEnum}.
     * <p>
     * This validation ensures that only known application roles
     * are used to determine the redirection target.
     *
     * @param role the role to validate
     * @return {@code true} if the role exists in {@code RoleEnum},
     *         {@code false} otherwise
     */
    private static boolean isAnExistingRole(String role) {
        return Arrays.stream(RoleEnum.values())
                .map(RoleEnum::getValue)
                .anyMatch(role::equals);
    }
}
