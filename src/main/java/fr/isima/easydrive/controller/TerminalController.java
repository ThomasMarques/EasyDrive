package fr.isima.easydrive.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import fr.isima.easydrive.ejb.UserService;
import fr.isima.easydrive.entity.User;

import java.io.Serializable;

@ManagedBean(name="terminalController")
@SessionScoped
public class TerminalController implements Serializable{
	
	@EJB
	private UserService us;

	public String handleCommand(String command, String[] params) {

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

		if(command.equals("ls")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(connected)  
				return "Florian is the best";  
			else  
				return "connection failed";  
		}  
		else if(command.equals("pwd"))
		{
			if(connected)  
				return "server side.";
			else  
				return "connection failed";
		}
		else if(command.equals("clear") || command.equals("server") || command.equals("local"))
			return "";
		else if(command.equals("test"))
		{
			if(connected)
				return "test : " + us.getUser(1).getLogin();
			else
				return "connection failed";
		}
		else if(command.equals("login"))
		{
            User u = null;

            try{
			    u = us.getUser(params[0]);
            }
            catch (Exception e)
            {
                return "connection failed";
            }

			if(u.checkPassword(params[1]))
            {
                if(session != null)
                    session.invalidate();
                session = (HttpSession) context.getExternalContext().getSession(true);
                session.setAttribute("connected", true);
                session.setAttribute("user_id", u.getIdUser());
                session.setAttribute("current_path", "/");
                return "connection ok";
            }
			else
				return "connection failed ";
		}
		else
		{
			if(connected)
				return "is not a server command.";  
			else
				return "connection failed";
		}
	}
}   
