package ru.azat.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.azat.models.Image;
import ru.azat.models.ImageStatus;

import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {

    private Long id;

    private String name;

    private String description;

    private String dataBase64;

    private ImageStatus status;

    private Long userId;

    public static ImageDto from(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .name(image.getName())
                .description(image.getDescription())
                .dataBase64(Base64.getEncoder().encodeToString(image.getData()))
                .status(image.getStatus())
                .userId(image.getUploadedUser().getId())
                .build();
    }
}
