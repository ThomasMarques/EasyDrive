package fr.isima.easydrive.controller;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import fr.isima.easydrive.ejb.FileService;
import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.User;
import org.primefaces.event.FileUploadEvent;

import fr.isima.easydrive.ejb.UserService;

import java.io.Serializable;
import java.util.Date;

@ManagedBean(name="fileUploadController")
@SessionScoped
public class FileUploadController implements Serializable {
	
	@EJB
	private UserService us;
    @EJB
    private FileService fs;

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
            String idUser = (String)session.getAttribute("user_id");
            Date now = new Date();

            User u = us.getUserById(idUser);
            FrontFile ff = new FrontFile();
            BackFile bf = new BackFile();

            bf.setCreationDate(now);
            bf.setData(event.getFile().getContents());
            bf.setSize((int) event.getFile().getSize());
            bf.setLastModificationDate(now);
            bf.setName(event.getFile().getFileName());

            ff.setAbsPath(folder + event.getFile().getFileName());
            ff.setUser(u);
            ff.setBackFile(bf);

            bf.addFrontFile(ff);

            fs.persistFrontFile(ff);
            fs.persistBackFile(bf);

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