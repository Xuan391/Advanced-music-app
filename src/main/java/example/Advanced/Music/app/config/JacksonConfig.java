package example.Advanced.Music.app.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import example.Advanced.Music.app.constans.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Configuration
public class JacksonConfig extends SimpleModule {
	private static final long serialVersionUID = 7761550456481622275L;

	@Autowired
	private SimpleDateFormat sdf;

	@Override
	public void setupModule(SetupContext context) {
		SimpleSerializers serializers = new SimpleSerializers();

		serializers.addSerializer(Calendar.class, new CalendarSerializer(false, sdf));
		serializers.addSerializer(Date.class, new DateSerializer(false, sdf));
		context.addSerializers(serializers);
	}
	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule());
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
//		om.setSerializationInclusion(Include.NON_NULL);
		// Optionally, configure additional features
//		om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// Register the LocalDateSerializer here
		SimpleModule customSerializersModule = new SimpleModule();
		customSerializersModule.addSerializer(Calendar.class, new CalendarSerializer(false, sdf));
		customSerializersModule.addSerializer(Date.class, new DateSerializer(false, sdf));
		customSerializersModule.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT)));
		customSerializersModule.addSerializer(LocalDate.class,
				new LocalDateSerializer(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT)));
		om.registerModule(customSerializersModule);
		customSerializersModule.addSerializer(Number.class, new CustomDoubleSerializer());
		return om;
	}

	public class CustomDoubleSerializer extends JsonSerializer<Number> {
		private DecimalFormat decimalFormat = new DecimalFormat("#.########");

		@Override
		public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			BigDecimal decimalValue = BigDecimal.valueOf(value.doubleValue());
			gen.writeNumber(decimalFormat.format(decimalValue));
		}
	}

}
