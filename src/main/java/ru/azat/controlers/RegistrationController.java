package ru.azat.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.azat.forms.UserForm;
import ru.azat.services.UsersService;

@RestController
@RequestMapping("/registration")
@PreAuthorize("isAnonymous()")
public class RegistrationController {

    @Autowired
    private UsersService usersService;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody UserForm userForm) {
        usersService.signUp(userForm);
        return ResponseEntity.ok().build();
    }
}
