package dev.yazidcv.identity;

import dev.yazidcv.organization.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/v1/admin") @PreAuthorize("hasRole('ADMIN')")
public class AdminController{
 private final AppUserRepository users; private final TeamRepository teams; private final OrganizationRepository organizations; private final TenantContext tenant; private final PasswordEncoder encoder; private final PasswordPolicy policy;
 public AdminController(AppUserRepository users,TeamRepository teams,OrganizationRepository organizations,TenantContext tenant,PasswordEncoder encoder,PasswordPolicy policy){this.users=users;this.teams=teams;this.organizations=organizations;this.tenant=tenant;this.encoder=encoder;this.policy=policy;}
 @GetMapping("/users") List<UserView> users(JwtAuthenticationToken auth){return users.findAllByOrganizationIdOrderByLastNameAscFirstNameAsc(tenant.organizationId(auth)).stream().map(UserView::of).toList();}
 @PostMapping("/users") @ResponseStatus(HttpStatus.CREATED) @Transactional UserView createUser(@Valid @RequestBody CreateUser r,JwtAuthenticationToken auth){UUID org=tenant.organizationId(auth);if(users.existsByEmailIgnoreCase(r.email()))throw new IllegalArgumentException("A user with this email already exists.");policy.validate(r.password(),r.email());UUID teamId=r.teamId();if(teamId!=null)teams.findByIdAndOrganizationId(teamId,org).orElseThrow(()->new IllegalArgumentException("Team does not belong to this organization."));var role=Role.valueOf(r.role().toUpperCase());return UserView.of(users.save(new AppUser(org,teamId,r.firstName(),r.lastName(),r.email(),encoder.encode(r.password()),role.name(),r.locale()==null?"en":r.locale())));}
 @DeleteMapping("/users/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Transactional void deactivate(@PathVariable UUID id,JwtAuthenticationToken auth){var u=users.findById(id).filter(x->x.getOrganizationId().equals(tenant.organizationId(auth))).orElseThrow();if(u.getEmail().equalsIgnoreCase(auth.getName()))throw new IllegalArgumentException("Administrators cannot deactivate their own account.");u.deactivate();}
 @GetMapping("/teams") List<TeamView> teams(JwtAuthenticationToken auth){return teams.findAllByOrganizationIdAndActiveTrueOrderByName(tenant.organizationId(auth)).stream().map(TeamView::of).toList();}
 @PostMapping("/teams") @ResponseStatus(HttpStatus.CREATED) TeamView createTeam(@Valid @RequestBody CreateTeam r,JwtAuthenticationToken auth){return TeamView.of(teams.save(new Team(tenant.organizationId(auth),r.name(),r.description())));}
 @GetMapping("/organization") OrganizationView organization(JwtAuthenticationToken auth){var o=organizations.findById(tenant.organizationId(auth)).orElseThrow();return new OrganizationView(o.getId(),o.getName(),o.getSlug(),o.isActive());}
 enum Role{ADMIN,RECRUITER,HIRING_MANAGER}
 record CreateUser(@NotBlank String firstName,@NotBlank String lastName,@Email @NotBlank String email,@NotBlank String password,@NotBlank String role,UUID teamId,@Pattern(regexp="en|fr|ar") String locale){}
 record CreateTeam(@NotBlank @Size(max=120) String name,@Size(max=500) String description){}
 record UserView(UUID id,String firstName,String lastName,String email,String role,UUID teamId,String locale,boolean active){static UserView of(AppUser u){return new UserView(u.getId(),u.getFirstName(),u.getLastName(),u.getEmail(),u.getRole(),u.getTeamId(),u.getLocale(),u.isActive());}}
 record TeamView(UUID id,String name,String description){static TeamView of(Team t){return new TeamView(t.getId(),t.getName(),t.getDescription());}}
 record OrganizationView(UUID id,String name,String slug,boolean active){}
}
