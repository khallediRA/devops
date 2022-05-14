package tn.esprit.spring;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.services.IEmployeService;


@SpringBootTest
class EmployeeServiceImplSecondPartTest {

    @Autowired
    private IEmployeService employeeService;



    @BeforeEach
    public void setUp() {
        
    }
    
    @Test
    void assertContrataddedInDB() {
        Contrat contrat = new Contrat();
        contrat.setTypeContrat("CDI");
        contrat.setReference(1);
        int referenceContract = employeeService.ajouterContrat(contrat);
        assertTrue(referenceContract > 0);
    }



   @Test
	void testGetNombreEmployeJPQL() {
		assertFalse(employeeService.getNombreEmployeJPQL() > 0);
    }
    

     @Test
      void testgetAllEmployes() {
        Employe employe = new Employe("Zaheg", "Mayssa", "mayssa.zaheg@esprit.tn", false, Role.INGENIEUR);
        employeeService.ajouterEmploye(employe);
         int lst = employeeService.getAllEmployes().size();
         assertThat(lst).isPositive();
     }	
}
