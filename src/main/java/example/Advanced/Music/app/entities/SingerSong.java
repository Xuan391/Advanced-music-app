package example.Advanced.Music.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "singer_song")
@Getter
public class SingerSong {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
////    @ManyToOne
////    @JoinColumn(name = "singer_id")
////    private Singer singer;
//    @NotNull
//    @Column(name = "singer_id", nullable = false)
//    private long singerId;
//
//    @JsonIgnore
//    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
//            targetEntity = Singer.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "singer_id", insertable = false, updatable = false, referencedColumnName = "id")
//    private Singer singer;
//
////    @ManyToOne
////    @JoinColumn(name = "song_id")
////    private Song song;
//    @NotNull
//    @Column(name = "song_id", nullable = false)
//    private long songId;
//
//    @JsonIgnore
//    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
//            targetEntity = Singer.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "song_id", insertable = false, updatable = false, referencedColumnName = "id")
//    private Song song;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            targetEntity = Singer.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "singer_id", referencedColumnName = "id")
    private Singer singer;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            targetEntity = Song.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", referencedColumnName = "id")
    private Song song;
}
