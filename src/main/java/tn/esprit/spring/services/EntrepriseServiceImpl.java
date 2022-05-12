package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;

@Service
@Slf4j
public class EntrepriseServiceImpl implements IEntrepriseService {
	private static final Logger log = LoggerFactory.getLogger(EntrepriseServiceImpl.class);
	@Autowired
	EntrepriseRepository entrepriseRepoistory;
	@Autowired
	DepartementRepository deptRepoistory;

	public int ajouterEntreprise(Entreprise entreprise) {
		log.info("ajouter l'entreprise ");
		log.debug("commencer a ajouté l'entreprise ");
		try{
			entrepriseRepoistory.save(entreprise);
		}catch (Exception e) {
			String error= "create enterprise error."+e.getMessage();
			log.error(error);
	   	} 
		   String createSuccessfully  = "create enterprise :"+entreprise.getName()+"added SUCCESSFULLY";
		   log.info(createSuccessfully);
		   return entreprise.getId();
	}

	public int ajouterDepartement(Departement dep) {
		log.debug("Add Department");
		try {
			deptRepoistory.save(dep);
		}
		catch (Exception e) {
			String createDepartmentErrorString ="create department error."+ e.getMessage();
			 log.error(createDepartmentErrorString);
		} 
		deptRepoistory.save(dep);
		String createDepartmentSuccessString ="create department :"+dep.getName()+"added SUCCESSFULLY";
		log.info(createDepartmentSuccessString);
		return dep.getId();
	}

	public void affecterDepartementAEntreprise(int depId, int entrepriseId) {
		// Le bout Master de cette relation N:1 est departement
		// donc il faut rajouter l'entreprise a departement
		// ==> c'est l'objet departement(le master) qui va mettre a jour l'association
		// Rappel : la classe qui contient mappedBy represente le bout Slave
		// Rappel : Dans une relation oneToMany le mappedBy doit etre du cote one.
	try {
		Entreprise entrepriseManagedEntity = entrepriseRepoistory.findById(entrepriseId)
				.orElseThrow(() -> new RuntimeException("entreprise with cannoot be found "));
		Departement depManagedEntity = deptRepoistory.findById(depId).orElseThrow(() -> new RuntimeException("cannot find departement id"));
		if(depManagedEntity!=null) {
			if(depManagedEntity.getEntreprise()==null) {
				depManagedEntity.setEntreprise(entrepriseManagedEntity);
			}else {
				depManagedEntity.getEntreprise().addDepartement(depManagedEntity);
			}
		deptRepoistory.save(depManagedEntity);
		}
	}catch(Exception error){
		log.error("Departement cannot be affected to enterprise");
	}
	}

	public List<String> getAllDepartementsNamesByEntreprise(int entrepriseId) {
		Entreprise entrepriseManagedEntity = entrepriseRepoistory.findById(entrepriseId).orElseThrow(() -> new RuntimeException("cannot find entreprise id"));
		List<String> depNames = new ArrayList<>();
		if(entrepriseManagedEntity!=null) {
			for (Departement dep : entrepriseManagedEntity.getDepartements()) {
				depNames.add(dep.getName());
			}
		}
		return depNames;
	}

	public void deleteEntrepriseById(int entrepriseId) {
		log.debug("Delete Enterprise");
		entrepriseRepoistory.delete(entrepriseRepoistory.findById(entrepriseId).orElseThrow(() -> new RuntimeException("cannot find entreprise id")));
	}

	public void deleteDepartementById(int depId) {
		deptRepoistory.delete(deptRepoistory.findById(depId).get());
	}

	public Entreprise getEntrepriseById(int entrepriseId) {
		return entrepriseRepoistory.findById(entrepriseId).get();
	}

}
