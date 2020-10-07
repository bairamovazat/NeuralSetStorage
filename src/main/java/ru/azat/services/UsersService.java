package ru.azat.services;

import org.springframework.stereotype.Service;
import ru.azat.forms.UserForm;
import ru.azat.models.User;

import java.util.List;

@Service
public interface UsersService {
    void signUp(UserForm userForm);

    List<User> findAll();

    User findOne(Long userId);
}
