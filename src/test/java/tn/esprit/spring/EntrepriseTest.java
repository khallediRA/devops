package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;
import tn.esprit.spring.services.EntrepriseServiceImpl;

@SpringBootTest
class EntrepriseTest {

	@Autowired
	EntrepriseServiceImpl entrepriseService;

	@Autowired
	EntrepriseRepository entrepriseRepoistory;
	
	@Autowired
	DepartementRepository departementRepository;

    @Test
    void assertEntrepriseAssertedInDB() {
        Entreprise entreprise = new Entreprise();
        entreprise.setName("vermeg");

		int entrepriseId = entrepriseService.ajouterEntreprise(entreprise);
		Optional<Entreprise> entrepriseSaved = entrepriseRepoistory.findById(entrepriseId);
		assertNotNull(entrepriseSaved);
    }
	@Test
    void assertDepartmentAssertedInDB() {
        Departement department = new Departement();
        department.setName("R&D");
		
		int entrepriseId = entrepriseService.ajouterDepartement(department);
		Optional<Departement> departmentSaved = departementRepository.findById(entrepriseId);
		assertNotNull(departmentSaved);
    }
	@Test
	void addDepartmentToTheEntreprise() {
		Entreprise entreprise = new Entreprise();
        entreprise.setName("vermeg");

		Departement department = new Departement();
        department.setName("R&D");

		Entreprise entrepriseSaved = entrepriseRepoistory.findById(entrepriseService.ajouterEntreprise(entreprise)).get();
		int entrepriseHasDepartemtntBefore = entrepriseSaved.getDepartements().size();
		entrepriseService.affecterDepartementAEntreprise(entrepriseService.ajouterEntreprise(entreprise), entrepriseService.ajouterDepartement(department));
		int entrepriseHasDepartemtntAfter = entrepriseSaved.getDepartements().size();
		assertEquals(entrepriseHasDepartemtntBefore, entrepriseHasDepartemtntAfter);

	}
}
