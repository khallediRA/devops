package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.ITimesheetService;
import tn.esprit.spring.services.TimesheetServiceImpl;


@SpringBootTest
 class TimeSheetTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimesheetServiceImpl.class);

	@Autowired
	ITimesheetService timesheetService;
	@Autowired
	IEmployeService employeService;
	@Autowired
	MissionRepository missionRepo ;

	
	Timesheet timesheet;
	Mission mission;
	Employe employe;

	@BeforeEach()
	void initializeInstance() {
		LOGGER.info("Starting Tests on Timesheet");

		Timesheet timesheet = new Timesheet();
		timesheet.setValide(true);

		Employe employe = new Employe();
		employe.setNom("Hmani");
		employe.setActif(true);
		
		Mission mission = new Mission();
		mission.setName("Dev");
		mission.setDescription("Dev Spring + angular");
		

		this.timesheet = timesheet;
		this.mission = mission;
		this.employe = employe;
		
	}
	
	@Test
	public void addTimesheetTest() {
		LOGGER.info("Starting Tests on Timesheet");
		LOGGER.info("Persisting Instances");

		employeService.ajouterEmploye(employe);
		Mission missiondto = new Mission();
		missiondto.setName("Mission Impossible :p ");
		
		Mission mission = missionRepo.save(missiondto);
		LOGGER.info("Persisting the newly created timesheet");
		TimesheetPK pk = timesheetService.ajouterTimesheet(mission.getId(), employe.getId(), new Date(), new Date()).getTimesheetPK();
		
		if (pk!=null) {
			LOGGER.info("Timesheet persisté avec succès");
			assertNotNull(pk);
		}
		else {
			LOGGER.error("Persistence problem for timesheet");
			fail();
		}
	}

	@Test
	public void testFindMissionsByEmploye() {
		LOGGER.info("Testing findAllMissionByEmployeJPQL Function");
		LOGGER.info("Fetching Missions for Employee " +employe.getNom()+ " Having ID = "+employe.getId());
		List<Mission> missions = timesheetService.findAllMissionByEmployeJPQL(employe.getId());
		if (missions.isEmpty()) {
			LOGGER.info("Employee has no missions");
		}
		else {
			LOGGER.info("Employee "+employe.getNom()+" is currently affected to these missions : ");
			missions.forEach( mission1 -> {
				assertNotNull(mission1);
				LOGGER.info(mission.getName());
			});
			;
		}
	}



}
