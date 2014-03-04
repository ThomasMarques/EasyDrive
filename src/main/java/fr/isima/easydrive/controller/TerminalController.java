package fr.isima.easydrive.controller;

import javax.faces.bean.ManagedBean;

@ManagedBean(name="terminalController")
public class TerminalController {  

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
		else  
			return "is not a server command.";  
	}  
}   
