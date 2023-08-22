package example.Advanced.Music.app.Util;

import example.Advanced.Music.app.dto.BaseResponse;
import example.Advanced.Music.app.dto.ErrorResponse;
import example.Advanced.Music.app.dto.PageResponse;
import example.Advanced.Music.app.dto.SuccessResponse;
import example.Advanced.Music.app.models.RequestContext;
import example.Advanced.Music.app.models.RequestContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RequestUtil {
	public static <T> SuccessResponse<T> ok(T data) {
		SuccessResponse<T> ret = new SuccessResponse<T>(data);
		getResponseData(ret, 1);
		return ret;
	}
	public static <T> PageResponse<T> page(Page<T> pageData) {
		PageResponse<T> ret = new PageResponse<T>(pageData);
		getResponseData(ret, 1);
		return ret;
	}

	public static ErrorResponse err(String errorCode, String errorMessage) {
		ErrorResponse ret = new ErrorResponse(errorCode, errorMessage);
		getResponseData(ret, 0);
		return ret;
	}

	private static void getResponseData(BaseResponse ret, int status) {
		Date responseTime = new Date();
		RequestContext ctx = RequestContextHolder.get();
		ret.setStatus(status);
		ret.setResponseTime(responseTime);
		ret.setProcessDuration(responseTime.getTime() - ctx.getReceivedTime());
		ret.setClientMessageId(ctx.getClientMessageId());
		ret.setClientTime(ctx.getClientTime());
	}
}
