package fr.isima.easydrive.controller;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;  

import fr.isima.easydrive.ejb.UserService;
  
@ManagedBean(name="fileUploadController")
@SessionScoped
public class FileUploadController {  
	
	@EJB
	private UserService services;
	
	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
}  