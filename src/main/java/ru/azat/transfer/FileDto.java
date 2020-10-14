package ru.azat.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.azat.models.File;
import ru.azat.models.Image;
import ru.azat.models.ImageStatus;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {

    private Long id;

    private String name;

    private String type;

    private byte[] data;

    public static FileDto from(File file) {
        return FileDto.builder()
                .id(file.getId())
                .name(file.getName())
                .type(file.getType())
                .data(file.getData())
                .build();
    }
}
