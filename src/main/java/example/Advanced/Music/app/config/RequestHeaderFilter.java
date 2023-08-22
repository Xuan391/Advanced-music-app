package example.Advanced.Music.app.config;

import com.google.common.base.Strings;
import example.Advanced.Music.app.enums.HeaderEnum;
import example.Advanced.Music.app.models.RequestContext;
import example.Advanced.Music.app.models.RequestContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestHeaderFilter extends OncePerRequestFilter {
    @Autowired
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        RequestContext ctx = new RequestContext();
        RequestContextHolder.set(ctx);
        String clientMessageId = request.getHeader(HeaderEnum.CLIENT_MESSAGE_ID.getLabel());
        if (Strings.isNullOrEmpty(clientMessageId)) {
            clientMessageId = UUID.randomUUID().toString();
        }
        ctx.setClientMessageId(clientMessageId);
        String clientTimeStr = request.getHeader(HeaderEnum.CLIENT_TIME.getLabel());
        if (!Strings.isNullOrEmpty(clientTimeStr)) {
            try {
                Date clientTime = simpleDateFormat.parse(clientTimeStr);
                ctx.setClientTime(clientTime);
            } catch (Exception e) {
            }
        }
        ctx.setReceivedTime(System.currentTimeMillis());
        filterChain.doFilter(request, response);
    }
}