package dev.yazidcv.identity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken; import org.springframework.stereotype.Component; import java.util.UUID;
@Component public class TenantContext {
 public static final UUID DEMO_ORG=UUID.fromString("a1000000-0000-0000-0000-000000000001");
 public UUID organizationId(JwtAuthenticationToken auth){return UUID.fromString(auth.getToken().getClaimAsString("org"));}
}
