package dev.yazidcv.identity;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service public class DatabaseUserDetailsService implements UserDetailsService{
 private final AppUserRepository users; public DatabaseUserDetailsService(AppUserRepository users){this.users=users;}
 public UserDetails loadUserByUsername(String email){var u=users.findByEmailIgnoreCase(email).orElseThrow(()->new UsernameNotFoundException("Invalid credentials"));return User.withUsername(u.getEmail()).password(u.getPasswordHash()).roles(u.getRole()).disabled(!u.isActive()).accountLocked(u.getLockedUntil()!=null&&u.getLockedUntil().isAfter(java.time.Instant.now())).build();}
}
