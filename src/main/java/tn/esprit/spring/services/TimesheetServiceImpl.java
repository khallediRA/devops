package tn.esprit.spring.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class TimesheetServiceImpl implements ITimesheetService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimesheetServiceImpl.class);

	@Autowired
	MissionRepository missionRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;
	@Autowired
	EmployeRepository employeRepository;
	
	public int ajouterMission(Mission mission) {
		missionRepository.save(mission);
		return mission.getId();
	}
    
	public void affecterMissionADepartement(int missionId, int depId) {
		LOGGER.info(" affecter mission a de partement en exécution ");
		LOGGER.debug("commencer a chercher id mission");
		Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission non trouvé"));
		LOGGER.debug("commencer a chercher id departement");
		Departement dep = deptRepoistory.findById(depId).orElseThrow(() -> new RuntimeException("Departement non trouvé"));
		LOGGER.debug("update departement");
		mission.setDepartement(dep);
		missionRepository.save(mission);
		LOGGER.debug("fin");
		
	}

	public Timesheet ajouterTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin) {
		LOGGER.info(" Debut ajouterTimesheet ");
		TimesheetPK timesheetPK = new TimesheetPK();
		timesheetPK.setDateDebut(dateDebut);
		timesheetPK.setDateFin(dateFin);
		timesheetPK.setIdEmploye(employeId);
		timesheetPK.setIdMission(missionId);
		LOGGER.debug("champ updated");
		
		Timesheet timesheet = new Timesheet();
		timesheet.setTimesheetPK(timesheetPK);
		timesheet.setValide(false); 
		LOGGER.info(" timesheet ajouter avec succées");
		return timesheetRepository.save(timesheet);
		
		
		
	}

	
	public void validerTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin, int validateurId) {
		LOGGER.info(" In valider Timesheet ");
		LOGGER.debug("commencer a chercher id employe validateur");
		Employe validateur = employeRepository.findById(validateurId).orElseThrow(() -> new RuntimeException("Validateur non trouvé"));
		LOGGER.debug("commencer a chercher id mission");
		Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission non trouvé"));
		LOGGER.debug("verifier s'il est un chef de departement");
		if(!validateur.getRole().equals(Role.CHEF_DEPARTEMENT)){
			LOGGER.debug("l'employe doit etre chef de departement pour valider une feuille de temps !");
			return;
		}
		LOGGER.debug("verifier s'il est le chef de departement de la mission en question");
		boolean chefDeLaMission = false;
		for(Departement dep : validateur.getDepartements()){
			if(dep.getId() == mission.getDepartement().getId()){
				chefDeLaMission = true;
				break;
			}
		}
		if(!chefDeLaMission){
			LOGGER.info("l'employe n'est pas un chef de la mission");
			LOGGER.debug("l'employe doit etre chef de departement de la mission en question");
			LOGGER.error("l'employer n'est pas un chef départtement");
			return;
			
		}
		LOGGER.debug("l'ajout de  timesheet");
		TimesheetPK timesheetPK = new TimesheetPK(missionId, employeId, dateDebut, dateFin);
		Timesheet timesheet =timesheetRepository.findBytimesheetPK(timesheetPK);
		timesheet.setValide(true);
		LOGGER.debug("update timesheet");
		
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String  message = "dateDebut : " + dateFormat.format(timesheet.getTimesheetPK().getDateDebut());
		LOGGER.debug(message);
		LOGGER.info("valider timesheet avec succées ");
		
	}

	
	public List<Mission> findAllMissionByEmployeJPQL(int employeId) {
		return timesheetRepository.findAllMissionByEmployeJPQL(employeId);
	}

	
	public List<Employe> getAllEmployeByMission(int missionId) {
		return timesheetRepository.getAllEmployeByMission(missionId);
	}

}
