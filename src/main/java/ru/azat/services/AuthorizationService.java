package ru.azat.services;

import ru.azat.models.User;

public interface AuthorizationService {
    User getCurrentUser();
}
