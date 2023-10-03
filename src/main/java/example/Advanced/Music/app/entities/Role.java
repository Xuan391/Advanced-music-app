package example.Advanced.Music.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import example.Advanced.Music.app.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "user_role",
//            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")},
//            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
//    private List<User> listRoles = new ArrayList<>();

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @OneToMany(mappedBy = "roleId", cascade = CascadeType.REMOVE)
//    private Set<UserRole> userRoles = new HashSet<>();

}