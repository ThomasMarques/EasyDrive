package fr.isima.tps.terminalController;

import java.util.Date;  

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
		else if(command.equals("date"))  
			return new Date().toString();  
		else  
			return command + " not found";  
	}  
}   
