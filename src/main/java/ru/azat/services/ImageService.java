package ru.azat.services;

import org.springframework.web.multipart.MultipartFile;
import ru.azat.models.File;
import ru.azat.models.Image;
import ru.azat.transfer.FileDto;
import ru.azat.transfer.ImageDto;

import java.io.IOException;

public interface ImageService {
    ImageDto getNextImage();

    ImageDto setRating(Long imageId, Float rating);

    ImageDto getImage(Long imageId);

    ImageDto createImage(MultipartFile file, ImageDto imageDto);

    FileDto getFile(Long imageId);

    void importZip(MultipartFile file) throws IOException;

}
