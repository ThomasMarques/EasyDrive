package fr.isima.easydrive.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;

import fr.isima.easydrive.ejb.Services;

@ManagedBean(name="terminalController")
@SessionScoped
public class TerminalController {  
	
	@EJB
	private Services services;
	
	public String handleCommand(String command, String[] params) {  
		if(command.equals("ls")) {  
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
			return services.getUser(1).getLogin();
		else  
			return "is not a server command.";  
	}
}   
