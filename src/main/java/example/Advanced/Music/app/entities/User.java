package example.Advanced.Music.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import example.Advanced.Music.app.constans.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) }) // ràng buộc cột username là duy nhất
public class User extends EntityBase{
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = Constants.NAME_MAX_LENGTH, min = Constants.NAME_MIN_LENGTH)
    @Pattern(regexp = Constants.PATTERN_USERNAME)
    @Column(name = "username", unique = true, nullable = false, length = Constants.NAME_MAX_LENGTH)
    private String username;

    @NotNull
    @Size(max = Constants.PASSWORD_MAX_LENGTH, min = Constants.PASSWORD_MIN_LENGTH)
    @JsonIgnore
    @Column(name = "password", nullable = false, length = Constants.PASSWORD_MAX_LENGTH)
    private String password;

    @NotNull
    @Size(max = Constants.DISPLAY_NAME_MAX_LENGTH, min = Constants.DISPLAY_NAME_MIN_LENGTH)
    @Pattern(regexp = Constants.PATTERN_NAME)
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @NotNull
    @Size(max = Constants.DISPLAY_NAME_MAX_LENGTH, min = Constants.DISPLAY_NAME_MIN_LENGTH)
    @Pattern(regexp = Constants.PATTERN_NAME)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Size(max = Constants.DISPLAY_NAME_MAX_LENGTH, min = Constants.DISPLAY_NAME_MIN_LENGTH)
    @Pattern(regexp = Constants.PATTERN_NAME)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @NotNull
    @Column(name = "is_lock", nullable = false)
    private Boolean isLock;

    @NotNull
    @Column(name = "email", nullable = false)
    @Email
    private String email;

    @ManyToMany(fetch = FetchType.LAZY/*, cascade = CascadeType.REMOVE*/)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private Set<Role> listRoles = new HashSet<>();

    @Min(Constants.FAIL_LOGIN_COUNT_MIN)
    @Max(Constants.FAIL_LOGIN_COUNT_MAX)
    @Column(name = "fail_login_count", nullable = false)
    private Integer failLoginCount;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Song> song;

    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Playlist> playlists = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ListenedHistory> listenedHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SearchHistory> searchHistories = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers = new HashSet<>();

    @Column(name = "quantity_follower")
    private int followersCount = 0;

    @PrePersist
    public void preInsert() {
        Playlist playlist = new Playlist();
        playlist.setName("Yêu thích");
        playlist.setCreator(this);
        playlist.setFavorite(true);
        playlist.setCreatedDate(Instant.now());
        this.playlists.add(playlist);

        this.displayName = this.firstName.trim() + " " + this.lastName.trim();
        this.failLoginCount = 0;
        if (isLock == null) {
            this.isLock = false;
        }
    }

    @PreUpdate
    public void preUpdateDisPlayName() {
        this.displayName = this.firstName.trim() + " " + this.lastName.trim();
    }
}
