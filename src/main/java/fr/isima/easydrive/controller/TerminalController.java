package fr.isima.easydrive.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;

import org.apache.commons.codec.digest.DigestUtils;

import fr.isima.easydrive.ejb.UserService;
import fr.isima.easydrive.entity.User;

@ManagedBean(name="terminalController")
@SessionScoped
public class TerminalController {  
	
	@EJB
	private UserService us;
	private boolean connected = false;
	
	public String handleCommand(String command, String[] params) {
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
			System.out.println("test controller");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//if(connected)  
				return "test : " + us.getUser(1).getLogin();
			//else  
			//	return "connection failed";
		}
		else if(command.equals("login"))
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			User u = new User();
			u.setHardPass("d9f3267977c85c3d0bfc0ae6406af0313c7313f8");
			u.setSalt("_?s>ONMVf4$yHU@%Sj$w");
			connected = true;
			
			if(params[0].equals("admin") && u.checkPassword(params[1]))
				return "connection ok";
			else
				return "connection failed";
		}
		else  
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(connected)
				return "is not a server command.";  
			else
				return "connection failed";
		}
	}
}   
