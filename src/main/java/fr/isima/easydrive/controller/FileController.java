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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

@ManagedBean(name="fileController")
@SessionScoped
public class FileController implements Serializable {
	
	@EJB
	private UserService userService;
    @EJB
    private FileService fileService;

    public StreamedContent getFile() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        String currentDir = (String)session.getAttribute("current_path");
        String userId = (String)session.getAttribute("user_id");
        String name = (String)session.getAttribute("download_file");

        session.removeAttribute("download_file");

        if(name == null)
            return null;

        FrontFile frontFile = fileService.getFile(currentDir, name, userId);

        BackFile backFile = frontFile.getBackFile();
        InputStream stream = new ByteArrayInputStream(backFile.getData());
        StreamedContent file = new DefaultStreamedContent(stream, "text/plain", backFile.getName());

        return  file;
    }

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
            String idUser = (String) session.getAttribute("user_id");
            Date now = new Date();

            User user = userService.getUserById(idUser);
            FrontFile frontFile = new FrontFile();
            BackFile backFile = new BackFile();

            backFile.setCreationDate(now);
            backFile.setData(event.getFile().getContents());
            backFile.setSize((int) event.getFile().getSize());
            backFile.setLastModificationDate(now);
            backFile.setName(event.getFile().getFileName());

            fileService.persistBackFile(backFile);

            frontFile.setAbsPath(folder);
            frontFile.setUser(user);
            frontFile.setBackFile(backFile);

            fileService.persistFrontFile(frontFile);

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