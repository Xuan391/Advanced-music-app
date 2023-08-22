package example.Advanced.Music.app.entities;

import example.Advanced.Music.app.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

}