/**
 * 
 */
package com.ivory.ivory;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.ivory.ivory.beans.Patient;
import com.ivory.ivory.beans.MedicalHistory;


/**
 * @author smahapat
 * 
 */
public class ManagePatient {

	/* Method to CREATE an patient in the database */
	public Integer addPatient(String fname, String lname, String email, String address, String dob, String gender, String phone, String mobile, Map<Integer, String> medicalHistory) {
		Session session = Hbutil.getSessionFactory().openSession();
		Transaction tx = null;
		Integer patientID = null;
		try {
			tx = session.beginTransaction();
			Patient patient = new Patient(fname, lname, email, address, dob, gender, phone, mobile);
			patientID = (Integer) session.save(patient);
			
			/* for each answer to a question create a MedicalHistory object
			 * and save it. Commit at the end of saving the user and all answers to medical
			 * history questions
			 */
			for (Map.Entry<Integer, String> entry : medicalHistory.entrySet()) {
			    int questionId = entry.getKey();
			    String answer = entry.getValue();
			    
			    // if the answer length is zero do not consider this question
			    if(answer.length() == 0){
			    	continue;
			    }
			    
			    MedicalHistory mh = new MedicalHistory(patientID, questionId, answer);
			    session.save(mh);
			}			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return patientID;
	}
	
	/**
	 * Searches for patients given a search string. The search is done for
	 * firstname, lastname, mobile, and email.
	 * @param searchString one of the above patient attributes like firstname, etc
	 * @return returns a list of patient objects
	 */
	@SuppressWarnings("unchecked")
	public List<Patient> searchPatient(String searchString){
		Session session = Hbutil.getSessionFactory().openSession();
		Transaction tx = null;
		List<Patient> patients = null;
		try{ 
			tx = session.beginTransaction();
			// Add restriction.
			Criterion fnameCr = Restrictions.like("firstName", searchString, MatchMode.ANYWHERE);
			Criterion lnameCr = Restrictions.like("lastName", searchString, MatchMode.ANYWHERE);
			Criterion mobileCr = Restrictions.like("mobile", searchString, MatchMode.EXACT);						
			Criterion emailCr = Restrictions.like("email", searchString, MatchMode.EXACT);
			Criterion completeCr = Restrictions.disjunction().add(fnameCr).add(lnameCr).add(mobileCr).add(emailCr);
			Criteria cr = session.createCriteria(Patient.class);
			cr.add(completeCr);			
			patients = cr.list();
			tx.commit();
		}catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return patients;
		
	}
	
	/**
	 * Gets the patient details like first name, address mobile etc
	 * @param id patient id
	 * @return Patient Object
	 */
	public Patient getPatientDetails(int id){
		Session session = Hbutil.getSessionFactory().openSession();
		Transaction tx = null;
		Patient patient = null;
		try{ 
			tx = session.beginTransaction();
			// Add restriction to get patient with id
			Criterion idCr = Restrictions.idEq(id);
			Criteria cr = session.createCriteria(Patient.class);
			cr.add(idCr);
			patient = (Patient)cr.uniqueResult();
			tx.commit();
		}catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return patient;		
	}
}