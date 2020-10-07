package ru.azat.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(indexes = {
        @Index(name = "index_name", columnList = "name"),
        @Index(name = "index_status", columnList = "status"),
})
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Lob
    @Lazy
    @Column
    private byte[] data;

    @Enumerated(value = EnumType.STRING)
    private ImageStatus status;

    @ManyToOne
    @JoinColumn(name = "uploaded_user_id")
    private User uploadedUser;

    @OneToMany(mappedBy = "image")
    private List<ImageVote> imageVoteList;
}
