package example.Advanced.Music.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import example.Advanced.Music.app.constans.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "singers")
public class Singer extends EntityBase{

    @NotNull
    @Size(max = Constants.NAME_MAX_LENGTH, min = 1)
    @Column(name = "singer_name")
    private String name;

}
