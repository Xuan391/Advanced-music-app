package example.Advanced.Music.app.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "playlist_song")
@Getter
public class PlaylistSong {
    @Id
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @Id
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
}
