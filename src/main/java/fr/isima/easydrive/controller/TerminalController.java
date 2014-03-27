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

                User user = userService.getUserByLogin(login);

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
            String userId = (String)session.getAttribute("user_id");
            switch(command)
            {
                case "cd" :
                    if(params.length == 1)
                    {
                        String path = params[0];
                        //get absolute path
                        path = fileService.getAbsolutePath(path, (String)session.getAttribute("current_path"));

                        if(path != null && fileService.folderExist(path, userId))
                        {
                            session.setAttribute("current_path", path);
                            response = "<span class=\"status-code\">[200]</span> " + path ;
                        }
                        else
                        {
                            response = "<span class=\"status-code\">[400]</span> The given path `" + params[0] + "` is not a directory or doesn't exist.";
                        }
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `cd directoryPath`, the path may be relative or absolute.";
                    }
                    break;
                case "ls" :
                    if(params.length == 0)
                    {
                        List<FrontFile> listChild = fileService.getAll(currentDir, userId);
                        response = "<span class=\"status-code\">[200]</span> "+currentDir;
                        response += formateListFiles(listChild);
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `ls`.";
                    }

                    break;

                case "pwd" :
                    if(params.length == 0)
                    {
                        response = "<span class=\"status-code\">[200]</span> Server side : " + currentDir;
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `pwd`.";
                    }
                    break;

                case "rm" :
                    if(params.length == 1)
                    {
                        response = "<span class=\"status-code\">[400]</span> Not implemented => remove a file specified in param.";
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `rm nameOfFileOrDirectoryToRemove`.";
                    }
                    break;

                case "mkdir" :
                    if(params.length == 1)
                    {
                        int requestResult = fileService.createDir(currentDir, params[0], userId);
                        /// Response analyser.
                        if(requestResult == 0)
                        {
                            response = "<span class=\"status-code\">[201]</span> Directory created.";
                        }
                        else if(requestResult == -1)
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
                case "cp" :
                    if(params.length == 2)
                    {
                        response = "<span class=\"status-code\">[400]</span> Not implemented => copy the file (param1) in the folder (param2).";
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `cp nameOfFileOrDirectoryToCopy location`.";
                    }
                    break;

                case "mv" :
                    if(params.length == 2)
                    {
                        response = "<span class=\"status-code\">[400]</span> Not implemented => move the file (param1) in the folder (param2).";
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `mv nameOfFileOrDirectoryToMove newLocation`.";
                    }
                    break;

                case "get" :
                case "download" :
                    if(params.length == 1)
                    {
                        response = "<span class=\"status-code\">[400]</span> Not implemented => download the file specified in param.";
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `download (or get) nameOfFileOrDirectoryToDownload`.";
                    }
                    break;

                case "find" :
                case "search" :
                    if(params.length == 1 || params.length == 2)
                    {
                        List<FrontFile> result = fileService.search(params[0], params.length == 2?params[1]:"", userId);
                        response = "<span class=\"status-code\">[400]</span> Not implemented => search a file containing (param1) from the current folder.";
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `find (or search) nameToSearch` to search in the current directory,  `find (or search) -a nameToSearch` to search in your all content.";
                    }
                    break;

                case "chmod" :
                case "share" :
                    if(params.length == 2)
                    {
                        response = "<span class=\"status-code\">[400]</span> Not implemented => share the file or folder (param1) with the user having to login (param2).";
                    }
                    else
                    {
                        response = "<span class=\"status-code\">[400]</span> Usage : `share (or chmod) nameOfFileOrDirectoryToShare userLogin`.";
                    }
                    break;

                ///
                case "add" :
                case "push" :
                case "status" : response = "<span class=\"status-code\">[404]</span> `" + command + "` is not a server command but a client command. ";
                    break;

                /// These local commands are not processed on the server side.
                case "local" :
                case "server" :
                case "clear" : response = "";
                    break;

                default :   response = "<span class=\"status-code\">[404]</span> `" + command + "` is not a server command.";
                    break;
            }
        }

        return response;
    }

    String formateListFiles(List<FrontFile> filesList)
    {
        String files = "";
        String directories = "";

        for(FrontFile frontFile : filesList)
        {
            if(frontFile.isDirectory())
                directories += "<br/>d-  " + frontFile.getBackFile().getName();
            else
                files += "<br/>-- " + frontFile.getBackFile().getName() + " <" + frontFile.getBackFile().getSize() + "B>";
        }
        return directories + files;
    }
}   
