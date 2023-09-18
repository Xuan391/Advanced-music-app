package example.Advanced.Music.app.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_download_song")
@Getter
@Setter
public class UserDownloadSong extends EntityBase {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private long userId;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
                targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "id")
    private User user;

    @NotNull
    @Column(name = "song_id", nullable = false)
    private long songId;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
                targetEntity = Song.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Song song;

}
