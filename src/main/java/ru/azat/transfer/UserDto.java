package ru.azat.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.azat.models.User;

import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@Builder
public class UserDto {
    public Long id;

    private String login;
    private String name;

    private String email;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> from(List<User> users) {
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }
}
