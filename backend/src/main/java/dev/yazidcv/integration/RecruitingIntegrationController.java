package dev.yazidcv.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/integrations")
public class RecruitingIntegrationController {
 private final String linkedInToken, linkedInOrganization, indeedClientId, indeedClientSecret;
 public RecruitingIntegrationController(@Value("${yazidcv.integrations.linkedin.access-token:}")String linkedInToken,@Value("${yazidcv.integrations.linkedin.organization-urn:}")String linkedInOrganization,@Value("${yazidcv.integrations.indeed.client-id:}")String indeedClientId,@Value("${yazidcv.integrations.indeed.client-secret:}")String indeedClientSecret){this.linkedInToken=linkedInToken;this.linkedInOrganization=linkedInOrganization;this.indeedClientId=indeedClientId;this.indeedClientSecret=indeedClientSecret;}
 @GetMapping public List<IntegrationStatus> statuses(){return List.of(
  new IntegrationStatus("LINKEDIN","LinkedIn Apply Connect",configured(linkedInToken,linkedInOrganization)?"CONFIGURED":"ACTION_REQUIRED",configured(linkedInToken,linkedInOrganization),"Approved LinkedIn Talent Solutions partner access, customer application, OAuth token, organization URN, and certification are required.","https://learn.microsoft.com/linkedin/talent/apply-connect/apply-connect-overview"),
  new IntegrationStatus("INDEED","Indeed Job Sync",configured(indeedClientId,indeedClientSecret)?"CONFIGURED":"ACTION_REQUIRED",configured(indeedClientId,indeedClientSecret),"An Indeed Partner Developer Agreement, approved integration request, and Partner Console OAuth credentials are required.","https://docs.indeed.com/job-sync-api/integrate-with-job-sync-api"));}
 private boolean configured(String... values){for(String value:values)if(value==null||value.isBlank())return false;return true;}
 public record IntegrationStatus(String provider,String name,String status,boolean configured,String requirement,String onboardingUrl){}
}
