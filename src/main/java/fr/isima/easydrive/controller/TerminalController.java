package fr.isima.easydrive.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import fr.isima.easydrive.ejb.FileService;
import fr.isima.easydrive.ejb.UserService;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.User;

import java.io.Serializable;
import java.util.List;

@ManagedBean(name="terminalController")
@SessionScoped
public class TerminalController implements Serializable{

    @EJB
    private UserService userService;
    @EJB
    private FileService fileService;

    public String handleCommand(String command, String[] params) {

        String response;
        boolean connected;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = null;

        try
        {
            session = (HttpSession) context.getExternalContext().getSession(false);
            connected = (boolean)session.getAttribute("connected");
        }
        catch (NullPointerException e)
        {
            connected = false;
        }

        if(!connected)
        {
            /// Client not connected.
            if(command.equals("login"))
            {
                String login = params[0];

                if(session != null)
                    session.invalidate();

                User user = userService.getUser(login);

                if(user == null)
                {
                    response = "<span class=\"status-code\">[401]</span> Login `" + login + "` does not exist.";
                }
                else
                {
                    if(user.checkPassword(params[1]))
                    {
                        session = (HttpSession) context.getExternalContext().getSession(true);
                        session.setAttribute("connected", true);
                        session.setAttribute("user_id", user.getIdUser());
                        session.setAttribute("current_path", "/");
                        response = "<span class=\"status-code\">[200]</span> Welcome " + login + ".";
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[401]</span> The password does not match.";
                    }
                }
            }
            else
            {
                response = "<span class=\"status-code\">[401]</span> Unauthorized : Client not connected.";
            }
        }
        else
        {
            /// Client connected.
            String currentDir = (String)session.getAttribute("current_path");
            switch(command)
            {
                case "ls" :
                    List<FrontFile> listChild = fileService.getAll(currentDir);
                    response = "<span class=\"status-code\">[200]</span> "+currentDir;

                    for(FrontFile frontFile : listChild)
                    {
                        response+= "<br/>" + frontFile.getBackFile().getName() + " <" + frontFile.getBackFile().getSize() + "B>";
                    }

                    break;

                case "pwd" : response = "<span class=\"status-code\">[200]</span> Server side.";
                    break;

                case "rm" : response = "<span class=\"status-code\">[400]</span> Not implemented => remove a file specified in param.";
                    break;

                case "mkdir" :
                    if(params.length == 1)
                    {
                        Long idUser = Long.parseLong((String) session.getAttribute("user_id"));
                        User user = userService.getUser(idUser);

                        int requestResult = fileService.createDir(currentDir, params[0], user);
                        if(requestResult == 0)
                        {
                            response = "<span class=\"status-code\">[201]</span> Directory created.";
                        }
                        else if(requestResult == 1)
                        {
                            response = "<span class=\"status-code\">[400]</span> Directory not created, directory with this name already exist.";
                        }
                        else
                        {
                            response = "<span class=\"status-code\">[400]</span> Directory not created.";
                        }
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `mkdir nameForNewDirectory`.";
                    }

                    break;

                case "cd" : response = "<span class=\"status-code\">[400]</span> Not implemented => allows you to change the current file (relative / absolute).";
                    break;

                case "cp" : response = "<span class=\"status-code\">[400]</span> Not implemented => copy the file (param1) in the folder (param2).";
                    break;

                case "get" :
                case "download" : response = "<span class=\"status-code\">[400]</span> Not implemented => download the file specified in param.";
                    break;

                case "find" :
                case "search" : response = "<span class=\"status-code\">[400]</span> Not implemented => search a file containing (param1) from the current folder.";
                    break;

                case "chmod" :
                case "share" : response = "<span class=\"status-code\">[400]</span> Not implemented => share the file or folder (param1) with the user having to login (param2).";
                    break;

                /// Local commands are not processed on the server side.
                case "local" :
                case "server" :
                case "clear" : response = "";
                    break;

                default :   response = "<span class=\"status-code\">[400]</span> `" + command + "` is not a server command.";
                    break;
            }
        }

        return response;
    }
}   
