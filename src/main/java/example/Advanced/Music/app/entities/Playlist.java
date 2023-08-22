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
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist extends EntityBase {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = Constants.NAME_MAX_LENGTH, min = Constants.NAME_MIN_LENGTH)
    @Pattern(regexp = Constants.PATTERN_NAME)
    @Column(name = "playlist_name", length = Constants.NAME_MAX_LENGTH)
    private String name;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "created_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Song> songs = new HashSet<>();

}
