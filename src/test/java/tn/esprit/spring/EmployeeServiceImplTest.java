package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.services.IEmployeService;

@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private IEmployeService employeeService;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private DepartementRepository departementRepository;



    @Test
    void employeeShouldBePersisted() {
        Employe employe = new Employe();
        employe.setPrenom("prenom");
        employe.setNom("nom");
        employe.setEmail("email");

        int idEmployee = employeeService.ajouterEmploye(employe);

        assertEquals(idEmployee, employeeService.ajouterEmploye(employe));
    }

    @Test
    void employeEmailShouldBeModfied() {
        Employe employe = new Employe();
        employe.setPrenom("prenom");
        employe.setNom("nom");
        employe.setEmail("email");
        int employeeId = employeeService.ajouterEmploye(employe);
        employeeService.mettreAjourEmailByEmployeId("emailupdated", employeeId);
        assertEquals("emailupdated", employeRepository.findById(employeeId).get().getEmail());
    }

    @Test
    void employeeShouldBeAffectedToDepartment() {
        Employe e = new Employe();
        e.setNom("e");
        Departement departement = new Departement();
        departement.setName("Department 1");
        int depId = departementRepository.save(departement).getId();
        int employeId = employeeService.ajouterEmploye(e);
        employeeService.affecterEmployeADepartement(employeId, depId);
        assertNotEquals(0, departementRepository.findById(depId).get().getEmployes().size());
    }

}