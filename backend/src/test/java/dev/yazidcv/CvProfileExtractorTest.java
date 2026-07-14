package dev.yazidcv;
import dev.yazidcv.cv.CvProfileExtractor;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CvProfileExtractorTest {
 private final CvProfileExtractor extractor=new CvProfileExtractor();
 @Test void parsesTwoColumnBilingualCvEvidence(){
  String cv="""
   COMPÉTENCES
   LANGUES
   EXPÉRIENCE PROFESSIONNELLE
   EL EXEMPLE EL AMINE
   CONTACT
   +212 600 00 00 00
   amine@example.test
   Marrakech - MAROC
   Ingénieur en informatique et réseaux – Profil full stack orienté produit et systèmes distribués avec une forte expérience de livraison.
   Développeur Full Stack Novembre 2025 - Présent
   Example Studio - Marrakech
   Technologies : Java - Spring Boot - React.js - Next.js - TypeScript - Docker - PostgreSQL - Flutter
   Arabe : Langue Maternelle
   Français : Courante
   Anglais : Courante
   Ecole Marocaine des sciences de l’ingénieur 2022-2025
   Diplôme ingénieur d’état en informatique et réseaux
   Tech Portfolio :
   Next.js, TypeScript, Tailwind CSS
   https://www.linkedin.com/in/el-amine-el-exemple-123/
   https://github.com/example
   """;
  var p=extractor.parse(cv,"amine-fr.pdf");
  assertThat(p.firstName()).isEqualTo("El Amine");assertThat(p.lastName()).isEqualTo("El Exemple");assertThat(p.currentTitle()).isEqualTo("Développeur Full Stack");assertThat(p.city()).isEqualTo("Marrakech");assertThat(p.country()).isEqualTo("Morocco");assertThat(p.languages()).containsExactlyInAnyOrder("Arabic","French","English");assertThat(p.skills()).contains("Java","Spring Boot","React.js","Next.js","TypeScript","Docker","PostgreSQL","Flutter");assertThat(p.education()).contains("Diplôme");assertThat(p.confidence()).isGreaterThanOrEqualTo(90);
 }
}
