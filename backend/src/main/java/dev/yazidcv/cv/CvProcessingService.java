package dev.yazidcv.cv;

import dev.yazidcv.candidate.*;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.*;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

@Service
public class CvProcessingService {
 private static final Pattern EMAIL=Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}",Pattern.CASE_INSENSITIVE);
 private static final Pattern PHONE=Pattern.compile("(?:\\+?\\d[\\d .()/-]{7,}\\d)");
 private static final List<String> KNOWN_SKILLS=List.of("Java","Spring Boot","JavaScript","TypeScript","React","Angular","Vue","Python","C#",".NET","PHP","Laravel","AWS","Azure","Google Cloud","Docker","Kubernetes","Terraform","Kafka","RabbitMQ","PostgreSQL","MySQL","MongoDB","Redis","Git","Jenkins","Maven","Gradle","JUnit","Selenium","Cypress","REST","GraphQL","Microservices","DevOps","Scrum");
 private final CvDocumentRepository documents; private final CandidateRepository candidates; private final AutoDetectParser parser=new AutoDetectParser();
 public CvProcessingService(CvDocumentRepository documents,CandidateRepository candidates){this.documents=documents;this.candidates=candidates;}
 @Transactional public CvDocument process(UUID documentId,Path file){CvDocument document=documents.findById(documentId).orElseThrow();document.processing();documents.save(document);try{String text=extract(file).replace("\u0000","").trim();if(text.length()<80){document.ocrRequired("This CV contains too little readable text. Tesseract OCR with eng, fra, and ara language packs is required for image-only pages.");return documents.save(document);}Candidate candidate=resolveCandidate(document,text);document.completed(candidate.getId(),text);return documents.save(document);}catch(Exception e){document.failed("Text extraction failed: "+safe(e.getMessage()));return documents.save(document);}}
 private String extract(Path file)throws Exception{var handler=new BodyContentHandler(2_000_000);var metadata=new Metadata();var context=new ParseContext();var ocr=new TesseractOCRConfig();ocr.setLanguage("eng+fra+ara");context.set(TesseractOCRConfig.class,ocr);try(var input=Files.newInputStream(file)){parser.parse(input,handler,metadata,context);}return handler.toString();}
 private Candidate resolveCandidate(CvDocument doc,String text){String email=match(EMAIL,text);if(doc.getCandidateId()!=null)return candidates.findByIdAndOrganizationId(doc.getCandidateId(),doc.getOrganizationId()).orElseThrow();if(email!=null){var existing=candidates.findFirstByOrganizationIdAndEmailIgnoreCase(doc.getOrganizationId(),email);if(existing.isPresent())return existing.get();}String[] name=detectName(text,doc.getOriginalFilename());Candidate c=new Candidate(doc.getOrganizationId(),name[0],name[1]);String phone=match(PHONE,text);List<String> skills=KNOWN_SKILLS.stream().filter(s->Pattern.compile("(?i)(?<![\\p{L}\\p{N}])"+Pattern.quote(s)+"(?![\\p{L}\\p{N}])").matcher(text).find()).toList();String title=detectTitle(text);String summary=text.replaceAll("\\s+"," ");if(summary.length()>900)summary=summary.substring(0,900)+"…";c.update(name[0],name[1],email,phone,title,null,null,null,0,"NEW",summary,skills);return candidates.save(c);}
 private String[] detectName(String text,String filename){for(String line:text.split("\\R")){String clean=line.trim().replaceAll("\\s+"," ");if(clean.length()>=3&&clean.length()<=70&&!clean.contains("@")&&clean.matches("[\\p{L} .'-]+")){String[] parts=clean.split(" ",2);return new String[]{title(parts[0]),parts.length>1?title(parts[1]):"Candidate"};}}String base=filename.replaceFirst("(?i)\\.(pdf|docx?|rtf)$","").replaceAll("[_-]+"," ").trim();String[] parts=base.split(" ",2);return new String[]{title(parts.length>0?parts[0]:"New"),parts.length>1?title(parts[1]):"Candidate"};}
 private String detectTitle(String text){for(String line:text.split("\\R")){String s=line.trim();if(s.length()>3&&s.length()<100&&s.matches("(?i).*(engineer|developer|manager|architect|designer|analyst|consultant|recruiter|specialist|technician|director|responsable|ingénieur|développeur|مدير|مهندس|مطور).*") )return s;}return "Candidate";}
 private String match(Pattern p,String text){Matcher m=p.matcher(text);return m.find()?m.group().trim():null;} private String title(String s){if(s.isBlank())return "Candidate";return s.substring(0,1).toUpperCase()+s.substring(1).toLowerCase();} private String safe(String s){return s==null?"Unknown parser error":s.substring(0,Math.min(900,s.length()));}
}
