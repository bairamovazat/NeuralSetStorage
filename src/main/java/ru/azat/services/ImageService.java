package ru.azat.services;

import ru.azat.models.Image;
import ru.azat.transfer.ImageDto;

public interface ImageService {
    ImageDto getNextImage();

    ImageDto setRating(Long imageId, Float rating);

    ImageDto createImage(ImageDto imageDto);

    ImageDto getImage(Long imageId);

    void generateRandom(int count);

}
