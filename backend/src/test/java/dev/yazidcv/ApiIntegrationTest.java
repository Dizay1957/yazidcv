package dev.yazidcv;

import dev.yazidcv.cv.*;
import dev.yazidcv.identity.TenantContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.nio.file.*;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiIntegrationTest {
 @Autowired TestRestTemplate http;
 @Autowired CvDocumentRepository documents;
 @Autowired CvProcessingService processing;
 @TempDir Path temp;
 @Test void loginProtectsScopesDataAndProcessesCv() throws Exception {
  var denied=http.getForEntity("/api/v1/candidates",String.class);assertThat(denied.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  var login=http.postForEntity("/api/v1/auth/login",Map.of("email","admin@yazidcv.dev","password","YazidCV2026!"),Map.class);assertThat(login.getStatusCode()).isEqualTo(HttpStatus.OK);
  String token=(String)login.getBody().get("accessToken");var headers=new HttpHeaders();headers.setBearerAuth(token);
  var candidates=http.exchange("/api/v1/candidates?size=10",HttpMethod.GET,new HttpEntity<>(headers),Map.class);assertThat(candidates.getStatusCode()).isEqualTo(HttpStatus.OK);assertThat(candidates.getBody().get("totalElements")).isEqualTo(4);
  var dashboard=http.exchange("/api/v1/dashboard",HttpMethod.GET,new HttpEntity<>(headers),Map.class);assertThat(dashboard.getStatusCode()).isEqualTo(HttpStatus.OK);assertThat(dashboard.getBody()).containsEntry("openJobs",3).containsEntry("awaitingReview",3);
  Path cv=temp.resolve("fatima-zahra-cv.txt");Files.writeString(cv,"Fatima Zahra El Idrissi\nSenior Java Engineer\nfatima.zahra@example.com\n+212 612 345 678\nJava Spring Boot PostgreSQL Docker Kafka\nExperienced engineer building secure distributed systems and recruitment platforms.");
  var document=documents.save(new CvDocument(TenantContext.DEMO_ORG,null,"fatima-zahra-cv.pdf","test/fatima-zahra-cv.pdf","application/pdf",Files.size(cv),UUID.randomUUID().toString(),"test@yazidcv.dev"));
  var processed=processing.process(document.getId(),cv);assertThat(processed.getStatus()).isEqualTo("COMPLETED");assertThat(processed.getCandidateId()).isNotNull();assertThat(processed.getExtractedText()).contains("fatima.zahra@example.com");
 }
}
