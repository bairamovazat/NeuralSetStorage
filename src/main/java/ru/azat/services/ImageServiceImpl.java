package ru.azat.services;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.azat.models.*;
import ru.azat.repositories.ImageRepository;
import ru.azat.repositories.UsersRepository;
import ru.azat.transfer.FileDto;
import ru.azat.transfer.ImageDto;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    public void importZip(MultipartFile file) throws IOException {

        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());

        ZipEntry entry = null;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            String name = entry.getName();

            if(!entry.isDirectory() && name.endsWith(".jpg")) {
                createImageFromZipEntry(entry, zipInputStream);

            }
        }
    }

    public void createImageFromZipEntry(ZipEntry zipEntry, ZipInputStream zipInputStream) throws IOException {
        byte[] fileData = IOUtils.toByteArray(zipInputStream);

        transactionTemplate.execute((transactionStatus -> {
            User user = authorizationService.getCurrentUser();

            String fileName = StringUtils.cleanPath(zipEntry.getName());
            File file;
            file = File.builder()
                    .name(fileName)
                    .type(null)
                    .data(fileData)
                    .build();

            Image image = Image.builder()
                    .name(fileName)
                    .description(zipEntry.getName())
                    .file(file)
                    .status(ImageStatus.NEW)
                    .uploadedUser(user == null ? null : usersRepository.findOne(user.getId()))
                    .build();

            return ImageDto.from(imageRepository.save(image));
        }));

    }

    @Override
    @Transactional
    public ImageDto createImage(MultipartFile multipartFile, ImageDto imageDto) {
        User user = authorizationService.getCurrentUser();

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        File file;
        try {
            file = File.builder()
                    .name(fileName)
                    .type(multipartFile.getContentType())
                    .data(multipartFile.getBytes())
                    .build();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        Image image = Image.builder()
                .name(imageDto.getName())
                .description(imageDto.getDescription())
                .file(file)
                .status(ImageStatus.NEW)
                .uploadedUser(user == null ? null : usersRepository.findOne(user.getId()))
                .build();

        return ImageDto.from(imageRepository.save(image));
    }

    @Override
    @Transactional
    public FileDto getFile(Long imageId) {
        Image image = imageRepository.findOne(imageId);
        return FileDto.from(image.getFile());
    }


    @Override
    @Transactional
    public ImageDto getImage(Long imageId) {
        return ImageDto.from(imageRepository.getOne(imageId));
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
        User user = authorizationService.getCurrentUser();
        user = user == null ? null : usersRepository.findOne(user.getId());

        Image image = imageRepository.findOne(imageId);
        image.setStatus(ImageStatus.VOTED);
        ImageVote imageVote = ImageVote.builder()
                .image(image)
                .voitedUser(user)
                .rating(rating)
                .build();
        image.getImageVoteList().add(imageVote);
        return ImageDto.from(image);
    }

}
