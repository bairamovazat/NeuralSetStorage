package ru.azat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.azat.models.Image;
import ru.azat.models.ImageStatus;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findFirstByStatusEquals(ImageStatus imageStatus);
}
