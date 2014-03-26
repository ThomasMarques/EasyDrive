package fr.isima.easydrive.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import fr.isima.easydrive.ejb.UserService;
import fr.isima.easydrive.entity.User;

import java.io.Serializable;
import java.security.InvalidParameterException;

@ManagedBean(name="terminalController")
@SessionScoped
public class TerminalController implements Serializable{

    @EJB
    private UserService userService;

    private User userConnected = null;

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
            switch(command)
            {
                case "ls" : response = "<span class=\"status-code\">[200]</span> List the files in the current folder.";
                    break;

                case "pwd" : response = "<span class=\"status-code\">[200]</span> Server side.";
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
