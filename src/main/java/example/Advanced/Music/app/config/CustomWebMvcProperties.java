package example.Advanced.Music.app.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
public class CustomWebMvcProperties extends WebMvcProperties {
	CustomWebMvcProperties() {
		getPathmatch().setMatchingStrategy(MatchingStrategy.ANT_PATH_MATCHER);
	}
}
