package dev.yazidcv.identity;
import org.springframework.stereotype.Component;
@Component public class PasswordPolicy{
 public void validate(String password,String email){if(password==null||password.length()<12||password.length()>128||!password.matches(".*[A-Z].*")||!password.matches(".*[a-z].*")||!password.matches(".*[0-9].*")||!password.matches(".*[^A-Za-z0-9].*")||email!=null&&password.toLowerCase().contains(email.substring(0,email.indexOf('@')).toLowerCase()))throw new IllegalArgumentException("Password must be 12–128 characters and include uppercase, lowercase, number, and symbol; it must not contain the email name.");}
}
