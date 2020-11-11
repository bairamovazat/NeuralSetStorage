package ru.azat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.azat.models.Image;
import ru.azat.models.ImageVote;

import java.util.List;

public interface ImageVoteRepository extends JpaRepository<ImageVote, Long> {

    @Query("from ImageVote v left join fetch v.image vi left join fetch vi.file")
    List<ImageVote> getAllAndEagerImage();
}
