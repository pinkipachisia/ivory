package com.ivory.ivory.rest;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.ivory.ivory.beans.Patient;
import com.ivory.ivory.ManageMedicalHistory;
import com.ivory.ivory.ManagePatient;
import com.ivory.ivory.beans.MedicalHistoryQuestion;

@RestController
@RequestMapping("/rest/patient")
public class PatientRestController {

	@RequestMapping(value = "/medical_history_questions", method = RequestMethod.GET)
	public List<MedicalHistoryQuestion> GetMedicalHistoryQuestions() {
		ManageMedicalHistory medicalHistory = new ManageMedicalHistory();
		List<MedicalHistoryQuestion> questions = medicalHistory.listQuestions();
		return questions;
	}

	@RequestMapping(value = "/{patientid}", method = RequestMethod.GET)
	public Patient GetPatient(@PathVariable int patientid) {
		ManagePatient mp = new ManagePatient();
		Patient patient = mp.getPatientDetails(patientid);
		return patient;
	}

	@RequestMapping(method = RequestMethod.POST)
	public Patient SavePatient(@RequestBody Patient patient) {
		ManagePatient mp = new ManagePatient();
		return mp.addPatient(patient);
	}

	@RequestMapping(value = "/{patientid}", method = RequestMethod.PUT)
	public Patient UpdatePatient(@RequestBody Patient patient) {
		ManagePatient mp = new ManagePatient();
		return mp.updatePatient(patient);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Patient> GetPatients() {
		ManagePatient mp = new ManagePatient();
		List<Patient> patients = mp.listPatients();
		return patients;
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Patient> SearchPatients(@RequestParam("term") String term) {
		ManagePatient mp = new ManagePatient();
		List<Patient> patients = mp.searchPatient(term);
		/*JSONObject obj = new JSONObject();
		obj.put("draw", new Integer(draw));
		obj.put("recordsTotal", new Integer(100));
		obj.put("recordsFiltered", new Integer(patients.size()));
		obj.put("data", patients);
		return obj.toString();*/
		return patients;
	}
}
