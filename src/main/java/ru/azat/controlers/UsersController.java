package ru.azat.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.azat.models.User;
import ru.azat.services.UsersService;
import ru.azat.transfer.UserDto;

import java.util.List;

import static ru.azat.transfer.UserDto.from;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping()
    public List<UserDto> getUsers() {
        return from(usersService.findAll());
    }

    @GetMapping("/{user-id}")
    public User getUser(@PathVariable("user-id") Long userId) {
        return usersService.findOne(userId);
    }

}