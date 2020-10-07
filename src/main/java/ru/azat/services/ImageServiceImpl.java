package ru.azat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.azat.models.Image;
import ru.azat.models.ImageStatus;
import ru.azat.models.User;
import ru.azat.repositories.ImageRepository;
import ru.azat.repositories.UsersRepository;
import ru.azat.transfer.ImageDto;
import ru.azat.utils.GenerateImageDataUtils;

import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    @Transactional
    public ImageDto createImage(ImageDto imageDto) {
        User user = authorizationService.getCurrentUser();

        Image image = Image.builder()
                .name(imageDto.getName())
                .description(imageDto.getDescription())
                .data(Base64.getDecoder().decode(imageDto.getDataBase64()))
                .status(ImageStatus.NEW)
                .uploadedUser(usersRepository.findOne(user.getId()))
                .build();

        return ImageDto.from(imageRepository.save(image));
    }

    @Override
    @Transactional
    public ImageDto getImage(Long imageId) {
        return ImageDto.from(imageRepository.getOne(imageId));
    }

    @Override
    public void generateRandom(int count) {
        for (int i = 0; i < count; i++) {
            final int index = i;
            transactionTemplate.execute((status) -> {
                createImage(ImageDto.builder()
                        .name("Generated " + index)
                        .description("Generated description " + index)
                        .dataBase64(GenerateImageDataUtils.generateBase64ImageData())
                        .build());
                return index;
            });
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ImageDto getNextImage() {
        reentrantLock.lock();

        Image image;
        //TODO перерефакторить
        try {
            image = imageRepository.findFirstByStatusEquals(ImageStatus.NEW).orElse(null);
            if (image != null) {
                image.setStatus(ImageStatus.OPEN);
            }
        } finally {
            reentrantLock.unlock();
        }

        return image == null ? null : ImageDto.from(image);

    }

    @Override
    @Transactional
    public ImageDto setRating(Long imageId, Float rating) {
        Image image = imageRepository.findOne(imageId);
        image.setStatus(ImageStatus.VOTED);
        return ImageDto.from(image);
    }

}
