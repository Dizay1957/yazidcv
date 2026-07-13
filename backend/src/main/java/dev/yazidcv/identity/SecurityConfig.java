package dev.yazidcv.identity;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration @EnableMethodSecurity
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(12);}
 @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration c)throws Exception{return c.getAuthenticationManager();}
 @Bean JwtEncoder jwtEncoder(@Value("${yazidcv.jwt-secret}") String secret){return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));}
 @Bean JwtDecoder jwtDecoder(@Value("${yazidcv.jwt-secret}") String secret){var key=new SecretKeySpec(secret.getBytes(),"HmacSHA256");return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();}
 @Bean JwtAuthenticationConverter jwtAuthenticationConverter(){var authorities=new org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter();authorities.setAuthoritiesClaimName("roles");authorities.setAuthorityPrefix("");var converter=new JwtAuthenticationConverter();converter.setJwtGrantedAuthoritiesConverter(authorities);return converter;}
 @Bean SecurityFilterChain security(HttpSecurity http,JwtAuthenticationConverter converter)throws Exception{return http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/api/v1/auth/login","/actuator/health/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll().anyRequest().authenticated()).oauth2ResourceServer(o->o.jwt(j->j.jwtAuthenticationConverter(converter))).headers(h->h.contentSecurityPolicy(c->c.policyDirectives("default-src 'none'; frame-ancestors 'none'")).frameOptions(f->f.deny()).referrerPolicy(r->r.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)).permissionsPolicyHeader(p->p.policy("camera=(), microphone=(), geolocation=()"))).build();}
 @Bean CorsConfigurationSource cors(@Value("${yazidcv.cors-origin}") String origin){var c=new CorsConfiguration();c.setAllowedOriginPatterns(List.of(origin,"http://localhost:*","http://127.0.0.1:*"));c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));c.setAllowedHeaders(List.of("Authorization","Content-Type","X-Correlation-ID"));c.setExposedHeaders(List.of("X-Correlation-ID"));var s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/**",c);return s;}
}
