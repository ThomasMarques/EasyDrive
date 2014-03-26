package fr.isima.easydrive.controller;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;  

import fr.isima.easydrive.ejb.UserService;
  
@ManagedBean(name="fileUploadController")
@SessionScoped
public class FileUploadController {  
	
	@EJB
	private UserService service;

	public void handleFileUpload(FileUploadEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = null;
        boolean connected;

        try
        {
            session = (HttpSession) context.getExternalContext().getSession(false);
            connected = (boolean)session.getAttribute("connected");
        }
        catch (NullPointerException e)
        {
            connected = false;
        }

        if(connected)
        {
            String folder = (String)session.getAttribute("current_path");
            FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded in folder " + folder);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else
        {
            FacesMessage msg = new FacesMessage("error you have to be connected.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }  
}  