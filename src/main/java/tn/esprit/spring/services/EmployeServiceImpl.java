package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;


@Service
public class EmployeServiceImpl implements IEmployeService {
	private static final Logger log = LoggerFactory.getLogger(EmployeServiceImpl.class);

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	public int ajouterEmploye(Employe employe) {
		employeRepository.save(employe);
		log.info("employee added" );
		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		Employe employe = employeRepository.findById(employeId)
				.orElseThrow(() -> new RuntimeException("cannot find employee with id" + employeId));
		employe.setEmail(email);
		employeRepository.save(employe);
		log.info("employee has changed his email  ");
	}

	public void affecterEmployeADepartement(int employeId, int depId) {
		Departement depManagedEntity = deptRepoistory.findById(depId)
				.orElseThrow(() -> new RuntimeException("cannot find department with id" + depId));
		Employe employeManagedEntity = employeRepository.findById(employeId)
				.orElseThrow(() -> new RuntimeException("cannot find Employe with id" + employeId));

		if (depManagedEntity.getEmployes() == null) {
			List<Employe> employes = new ArrayList<>();
			employes.add(employeManagedEntity);
			depManagedEntity.setEmployes(employes);


		} else {

			depManagedEntity.getEmployes().add(employeManagedEntity);


		}

		deptRepoistory.save(depManagedEntity);

	}
	@Transactional
	//supprimer l'employe du departement
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		try {
			Departement dep = deptRepoistory.findById(depId).orElse(null);
	  if (dep !=null) {
			int employeNb = dep.getEmployes().size();
			for(int index = 0; index < employeNb; index++){
				if(dep.getEmployes().get(index).getId() == employeId){
					dep.getEmployes().remove(index);
					break;
				}
			}
		}}
		catch(Exception e) {
			log.error("Erreur lors de la désaffectation employé departement");
		}
	
	}

	public int ajouterContrat(Contrat contrat) {
		try {
			contratRepoistory.save(contrat);
		}
		    catch (Exception exp) {
				String catchErrroString = "Ajout contrat error"+exp.getMessage();
		    	log.error(catchErrroString);
		    }
		        log.info("Contrat Ajouté avec succée");
				return contrat.getReference();
			

	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).orElse(null);
		Employe employeManagedEntity = employeRepository.findById(employeId).orElse(null);
		if (contratManagedEntity != null && employeManagedEntity != null) {
		contratManagedEntity.setEmploye(employeManagedEntity);
		contratRepoistory.save(contratManagedEntity);
		}
	}

	public String getEmployePrenomById(int employeId) {
		Employe employeManagedEntity = employeRepository.findById(employeId).orElse(null);
		if(employeManagedEntity !=null){
		 return employeManagedEntity.getPrenom();}
		else {
			return null;
		}

	}
	public void deleteEmployeById(int employeId)
	{
		Employe employe = employeRepository.findById(employeId).orElse(null);

		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		if(employe != null){
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}

		employeRepository.delete(employe);
	}}

	public void deleteContratById(int contratId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).orElse(null);
		if (contratManagedEntity != null) {
			contratRepoistory.delete(contratManagedEntity);
			} else {
				log.error("Contrat may be NULL");
			}


	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}
	
	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}
	
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
         employeRepository.deleteAllContratJPQL();
	}
	
	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}
	
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
				return (List<Employe>) employeRepository.findAll();
	}




}

