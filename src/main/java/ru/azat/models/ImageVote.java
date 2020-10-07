package ru.azat.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ImageVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float rating;

    @ManyToOne
    @JoinColumn(name = "voited_user_id")
    private User voitedUser;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
