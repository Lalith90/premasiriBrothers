package com.brothers.premasiri.resourse.companyResource.controller;/*
package com.excellenthealthSolution.pharmacy.resourse.companyResource.controller;

import com.excellenthealthSolution.pharmacy.common.service.DateTimeAgeService;
import com.excellenthealthSolution.pharmacy.common.service.EmailService;
import com.excellenthealthSolution.pharmacy.general.Security.service.UserService;
import com.excellenthealthSolution.pharmacy.general.consultation.entity.Enum.Gender;
import com.excellenthealthSolution.pharmacy.general.consultation.entity.Enum.Title;
import com.excellenthealthSolution.pharmacy.resourse.companyResource.entity.Customer;
import com.excellenthealthSolution.pharmacy.resourse.companyResource.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    private final DateTimeAgeService dateTimeAgeService;
    private final UserService userService;
    private final EmailService emailService;


    @Autowired
    public PatientController(PatientService patientService, DateTimeAgeService dateTimeAgeService, UserService userService, EmailService emailService) {
        this.patientService = patientService;
        this.dateTimeAgeService = dateTimeAgeService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @RequestMapping
    public String patientPage(Model model) {
        List<Customer> patients = patientService.findAll();
        model.addAttribute("patients", patients);
        return "patient/patient";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String patientView(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("patientDetail", patientService.findById(id));
        return "patient/patient-detail";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editPatientFrom(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("patient", patientService.findById(id));
        model.addAttribute("newPatient",patientService.findById(id).getNumber());
        model.addAttribute("addStatus", false);
        model.addAttribute("title", Title.values());
        model.addAttribute("gender", Gender.values());
        return "patient/addPatient";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String patientAddFrom(Model model) {
        String input =  patientService.lastPatient().getNumber();
        String patientNumber= input.replaceAll("[^0-9]+", "");
        Integer PatientNumber = Integer.parseInt(patientNumber);
        int newPatientNumber = PatientNumber+1;
        model.addAttribute("addStatus", true);
        model.addAttribute("lastPatient",input);
        model.addAttribute("newPatient","EHS"+ newPatientNumber);
        model.addAttribute("title", Title.values());
        Model gender = model.addAttribute("gender", Gender.values());
        model.addAttribute("patient", new Customer());
        return "patient/addPatient";
    }

    // Above method support to send data to front end - All List, update, edit
    //Bellow method support to do back end function save, delete, update, search

    @RequestMapping(value = {"/add","/update"}, method = RequestMethod.POST)
    public String addPatient(@Valid @ModelAttribute Customer patient, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = userService.findByUserIdByUserName(auth.getName());
        System.out.println(patient);
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                System.out.println(error.getField() + ": " + error.getDefaultMessage());
            }
            model.addAttribute("addStatus", true);
            model.addAttribute("title", Title.values());
            model.addAttribute("gender", Gender.values());
            model.addAttribute("patient", patient);
            return "/patient/addPatient";
        }
        if (patient.getId() != null){
            patient.setUpdatedAt(dateTimeAgeService.getCurrentDate());
            if(patient.getEmail() != null){
            String message = "Welcome to Excellent Health Solution \n " +
                    "Your detail is updated"+
                    "\n\n\n\n\n Please inform us to if there is any changes on your details" +
                    "\n Kindly request keep your data up to date with us. so we can provide better service for you." +
                    "\n \n \n   Thank You" +
                    "\n Excellent Health Solution";
         boolean isFlag = emailService.sendPatientRegistrationEmail(patient.getEmail(),"Welcome to Excellent Health Solution ", message);
        if (isFlag){
             redirectAttributes.addFlashAttribute("message", "Successfully Update and Email was sent.");
             redirectAttributes.addFlashAttribute("alertStatus",true);
             patientService.persist(patient);
             return "redirect:/patient";
        }else {
            redirectAttributes.addFlashAttribute("message", "Successfully Update but Email was not sent.");
            redirectAttributes.addFlashAttribute("alertStatus",false);
                patientService.persist(patient);
                return "redirect:/patient";
        }
            }

            patientService.persist(patient);
            return "redirect:/patient";
        }
        if (patient.getEmail() != null){
            String message = "Welcome to Excellent Health Solution \n " +
                    "Your registration number is "+patient.getNumber()+
                    "\nYour Details are"+
                    "\n "+patient.getTitle().getTitle()+" "+patient.getName()+
                    "\n "+patient.getNic()+
                    "\n "+patient.getDateOfBirth()+
                    "\n "+patient.getMobile()+
                    "\n "+patient.getLand()+
                    "\n\n\n\n\n Please inform us to if there is any changes on your details" +
                    "\n Kindly request keep your data up to date with us. so we can provide better service for you." +
                    "\n \n \n   Thank You" +
                    "\n Excellent Health Solution";
            boolean isFlag = emailService.sendPatientRegistrationEmail(patient.getEmail(),"Welcome to Excellent Health Solution ", message);
            if (isFlag){
                redirectAttributes.addFlashAttribute("message", "Successfully Add and Email was sent.");
                redirectAttributes.addFlashAttribute("alertStatus",true);
                patient.setCreatedAt(dateTimeAgeService.getCurrentDate());
                patientService.persist(patient);
                return "redirect:/patient";
            }else {
                redirectAttributes.addFlashAttribute("message", "Successfully Add but Email was not sent.");
                redirectAttributes.addFlashAttribute("alertStatus",false);
                patient.setCreatedAt(dateTimeAgeService.getCurrentDate());
                patientService.persist(patient);
                return "redirect:/patient";
            }
        }
        patient.setCreatedAt(dateTimeAgeService.getCurrentDate());
        patientService.persist(patient);
        return "redirect:/patient";
    }


    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public String removePatient(@PathVariable Integer id) {
        patientService.delete(id);
        return "redirect:/patient";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, Customer patient) {
        model.addAttribute("patientDetail", patientService.search(patient));
        return "patient/patient-detail";
    }

}
*/
