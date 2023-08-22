package example.Advanced.Music.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.enums.OTPType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "OTP")
@Table(name = "act_otp")
public class OTP {
	@Id
	@NotNull
	@Size(min = Constants.ID_MAX_LENGTH, max = Constants.ID_MAX_LENGTH)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = Constants.ID_MAX_LENGTH)
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private OTPType type;

	@NotNull
	@Column(name = "value", nullable = false)
	private String value;

	@NotNull
	@Column(name = "issue_date", columnDefinition = "TIMESTAMP")
	private Date issueDate;

	@NotNull
	@Column(name = "expire_date", columnDefinition = "TIMESTAMP")
	private Date expireDate;

	@NotNull
	@Column(name = "user_id", nullable = false, length = Constants.ID_MAX_LENGTH)
	@Size(max = Constants.ID_MAX_LENGTH, min = Constants.ID_MAX_LENGTH)
	private long userId;

	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, targetEntity = Users.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "id")
	private Users user;

}
