package example.Advanced.Music.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.enums.TokenTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "act_security_token", uniqueConstraints = { @UniqueConstraint(columnNames = "value") })
public class Token extends EntityBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int VALUE_MAX_LENGTH = 1024;
	private static final int VALUE_MIN_LENGTH = 32;
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "token_type")
	private TokenTypeEnum tokenType;

	@NotNull
	@Size(max = VALUE_MAX_LENGTH, min = VALUE_MIN_LENGTH)
	@Column(name = "value", nullable = false, length = VALUE_MAX_LENGTH)
	private String value;

	@NotNull
	@Column(name = "issue_date", columnDefinition = "TIMESTAMP")
	private Date issueDate;

	@NotNull
	@Column(name = "expire_date", columnDefinition = "TIMESTAMP")
	private Date expireDate;

	@NotNull
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, targetEntity = Users.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "id")
	private Users user;
}
