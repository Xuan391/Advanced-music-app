package example.Advanced.Music.app.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song extends EntityBase{
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "song_name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "singer_song",
            joinColumns = {@JoinColumn(name = "singer_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Singer> singers = new ArrayList<>();

    @NotNull
    @Column(name = "song_data_url", nullable = false, unique = true)
    private String songUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "lyric")
    private String lyric = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    @Column(name = "listened_count")
    private int listenedCount = 0;

    @OneToMany(mappedBy = "song", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<ListenedHistory> listenedHistories;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy = "song", cascade = CascadeType.REMOVE)
    private Set<PlaylistSong> playlistSongs = new HashSet<>();

}
