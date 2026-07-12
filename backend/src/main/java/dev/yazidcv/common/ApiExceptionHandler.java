package dev.yazidcv.common;
import jakarta.servlet.http.HttpServletRequest; import org.springframework.dao.DataIntegrityViolationException; import org.springframework.http.*; import org.springframework.security.authentication.BadCredentialsException; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import org.springframework.web.multipart.MaxUploadSizeExceededException; import java.time.Instant; import java.util.*;
@RestControllerAdvice public class ApiExceptionHandler{
 @ExceptionHandler(NoSuchElementException.class) ResponseEntity<ApiError> notFound(NoSuchElementException e,HttpServletRequest r){return error(HttpStatus.NOT_FOUND,"NOT_FOUND",e.getMessage(),r);}
 @ExceptionHandler(BadCredentialsException.class) ResponseEntity<ApiError> unauthorized(BadCredentialsException e,HttpServletRequest r){return error(HttpStatus.UNAUTHORIZED,"INVALID_CREDENTIALS","Email or password is incorrect",r);}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiError> invalid(MethodArgumentNotValidException e,HttpServletRequest r){String msg=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Invalid request");return error(HttpStatus.UNPROCESSABLE_ENTITY,"VALIDATION_FAILED",msg,r);}
 @ExceptionHandler({DataIntegrityViolationException.class,IllegalStateException.class}) ResponseEntity<ApiError> conflict(Exception e,HttpServletRequest r){return error(HttpStatus.CONFLICT,"CONFLICT",e.getMessage(),r);}
 @ExceptionHandler(MaxUploadSizeExceededException.class) ResponseEntity<ApiError> tooLarge(Exception e,HttpServletRequest r){return error(HttpStatus.PAYLOAD_TOO_LARGE,"FILE_TOO_LARGE","The uploaded file exceeds the configured limit",r);}
 private ResponseEntity<ApiError> error(HttpStatus s,String code,String msg,HttpServletRequest r){return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),code,msg,r.getRequestURI(),Optional.ofNullable(r.getHeader("X-Correlation-ID")).orElse("unavailable")));}
 public record ApiError(Instant timestamp,int status,String code,String message,String path,String correlationId){}
}
