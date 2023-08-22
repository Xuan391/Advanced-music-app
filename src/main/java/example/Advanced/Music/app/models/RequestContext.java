package example.Advanced.Music.app.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Data
@Getter
@Setter
public class RequestContext {
    private String clientMessageId;
    private Date clientTime;
    private long receivedTime;
}
