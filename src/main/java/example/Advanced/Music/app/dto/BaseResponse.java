package example.Advanced.Music.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.Date;

@Data
public class BaseResponse {
    private int status;
    private long processDuration;
    private Date responseTime;
    @JsonInclude(Include.NON_NULL)
    private Date clientTime;
    private String clientMessageId;
}