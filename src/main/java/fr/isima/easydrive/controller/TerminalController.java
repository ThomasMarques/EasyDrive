package fr.isima.easydrive.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;

import fr.isima.easydrive.ejb.UserService;

import java.io.Serializable;

@ManagedBean(name="terminalController")
@SessionScoped
public class TerminalController implements Serializable{
	
	@EJB
	private UserService us;
	
	public String handleCommand(String command, String[] params) {
		if(command.equals("ls")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(params.length > 0)  
				return "No root for " + params[0] + " but Florian is the best";  
			else  
				return "Hello Stranger remind you that Florian is legendary";  
		}  
		else if(command.equals("pwd"))  
			return "server side.";
		else if(command.equals("clear") || command.equals("server") || command.equals("local"))
			return "";
		else if(command.equals("test"))
			return "test : " + us.getUser(1).getLogin();
		else if(command.equals("login"))
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "connection ok";
		}
		else  
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "is not a server command.";  
		}
	}
}   
