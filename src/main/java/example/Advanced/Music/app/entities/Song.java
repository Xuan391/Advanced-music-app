package example.Advanced.Music.app.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import example.Advanced.Music.app.constans.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song extends EntityBase{
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = Constants.NAME_MAX_LENGTH, min = Constants.NAME_MIN_LENGTH)
    @Pattern(regexp = Constants.PATTERN_NAME)
    @Column(name = "song_name", length = Constants.NAME_MAX_LENGTH)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "singer_song",
            joinColumns = {@JoinColumn(name = "singer_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Singer> singers = new HashSet<>();

    @NotNull
    @Column(name = "song_data_url", nullable = false, unique = true)
    private String songUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "creator_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    @Column(name = "download_count")
    private int downloadCount;

    @Column(name = "listened_count")
    private int listenedCount;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<ListenedHistory> listenedHistories;

}
