package ru.azat.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.azat.models.User;
import ru.azat.security.authentications.TokenAuthentication;
import ru.azat.security.details.UserDetailsImpl;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (!(authentication instanceof TokenAuthentication)) {
            return null;
        }

        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

        UserDetailsImpl userDetails = (UserDetailsImpl) tokenAuthentication.getUserDetails();
        return userDetails.getUser();
    }
}
