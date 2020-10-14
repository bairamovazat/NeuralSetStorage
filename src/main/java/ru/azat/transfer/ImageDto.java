package ru.azat.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.azat.models.Image;
import ru.azat.models.ImageStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {

    private Long id;

    private String name;

    private String description;

    private ImageStatus status;

    private Long userId;

    public static ImageDto from(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .name(image.getName())
                .description(image.getDescription())
                .status(image.getStatus())
                .userId(image.getUploadedUser() == null ? null : image.getUploadedUser().getId())
                .build();
    }
}
