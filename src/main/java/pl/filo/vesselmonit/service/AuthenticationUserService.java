package pl.filo.vesselmonit.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class AuthenticationUserService
{
    public void setAuthorizationInfo(final ModelMap modelMap, final User authenticationUser) {
        final boolean logged = isAuthenticatedUser(authenticationUser);
        modelMap.addAttribute("isUserLogged", logged);
        if(logged)
        {
            modelMap.addAttribute("isAdminAuthorized", isAuthorizedAdmin(authenticationUser));
        }
    }

    private boolean isAuthenticatedUser(final User authenticationUser) {
        return authenticationUser != null;
    }

    private boolean isAuthorizedAdmin(final User authenticationUser) {
        return authenticationUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    public void adminAuthorized(final ModelMap modelMap) {
        modelMap.addAttribute("isUserLogged", true);
        modelMap.addAttribute("isAdminAuthorized", true);
    }

}