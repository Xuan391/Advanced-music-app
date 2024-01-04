package example.Advanced.Music.app.config;

import example.Advanced.Music.app.config.handler.AuthEntryPointJwt;
import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.jwt.JwtAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;

@ComponentScan
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Value("${app.api.allowed-origins}")
    private String[] allowedOrigin;

    @Autowired
    private AuthEntryPointJwt unAuthorizedHandler;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthFilter jwtAuthenticationFilter;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .requestMatchers("/app/**/*.{js,html}")
                .requestMatchers("/public/**", "css/**","js/**","/lib/**", "/favicon.ico")
                .requestMatchers("/actuator").requestMatchers("**/login").requestMatchers("/api/v1/public/**")
                .requestMatchers("/api/v1/users/forget-password").requestMatchers("/api/v1/users/forget-password-secret")
                .requestMatchers("/api/v1/users/reset-password").requestMatchers("/api/v1/users/reset-password-secret")
                .requestMatchers("/api/v1/users/register").requestMatchers("/api/v1/users/accept-register")
                .requestMatchers("/api/v1/imageFiles/**")
                .requestMatchers("/api/v1/music/**")
                .requestMatchers("/api/v1/users/login-secret").requestMatchers("/api/v1/public/**")
                .requestMatchers("/swagger-ui", "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(unAuthorizedHandler);
        http.cors(Customizer.withDefaults());
        http.authorizeRequests()
                .requestMatchers("/swagger-ui/index.html", "/swagger-resources/**", "/v2/api-docs").permitAll()
                .requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations())).permitAll()
                .requestMatchers("/api/v1/users/login").permitAll()
                .requestMatchers("/api/v1/imageFiles/**").permitAll()
                .requestMatchers("/api/v1/music/musicFiles/**").permitAll()
                .requestMatchers("/api/v1/users/**").hasAnyAuthority(Constants.Role.ROLE_ADMIN, Constants.Role.ROLE_USER)
                .requestMatchers("/api/v1/song/**").hasAnyAuthority(Constants.Role.ROLE_USER, Constants.Role.ROLE_ADMIN)
                .anyRequest().authenticated();
        http.authorizeRequests().and().logout().logoutUrl("/api/v1/users/logout")
                .logoutSuccessHandler(logoutSuccessHandler);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.userDetailsService(userDetailsService);
        return http.build();
    }

    class CorsConfiguration implements CorsConfigurationSource{
        @Override
        public org.springframework.web.cors.CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
            org.springframework.web.cors.CorsConfiguration cors = new org.springframework.web.cors.CorsConfiguration();
            String[] methodArray = { "*" };
            String[] headerArray = { "*" };
            cors.setAllowedOrigins(Arrays.asList(allowedOrigin));
            cors.setAllowedMethods(Arrays.asList(methodArray));
            cors.setAllowedHeaders(Arrays.asList(headerArray));
            return cors;
        }
    }

}
