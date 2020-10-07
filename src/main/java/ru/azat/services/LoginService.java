package ru.azat.services;

import org.springframework.stereotype.Service;
import ru.azat.forms.LoginForm;
import ru.azat.transfer.TokenDto;

@Service
public interface LoginService {
    TokenDto login(LoginForm loginForm);
}
