package example.Advanced.Music.app.entities;

import example.Advanced.Music.app.constans.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "singers")
public class Singer extends EntityBase{
    private static final long serialVersionUID = 1L;
    @NotNull
    @Size(max = Constants.NAME_MAX_LENGTH, min = 1)
    @Column(name = "singer_name")
    private String name;
}
