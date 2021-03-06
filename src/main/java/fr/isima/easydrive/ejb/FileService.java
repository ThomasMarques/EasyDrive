package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import fr.isima.easydrive.dao.FileAccessLayer;
import fr.isima.easydrive.dao.UserAccessLayer;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.User;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.security.InvalidParameterException;

import java.util.ArrayList;
import java.util.List;

@Singleton
@LocalBean
public class FileService {

    private FileAccessLayer fileDAL;
    private UserAccessLayer userDAL;
	
	public FileService() {
        fileDAL = new FileAccessLayer();
        userDAL = new UserAccessLayer();
	}

    public void persistFrontFile(FrontFile ff)
    {
        fileDAL.persistFrontFile(ff);
    }

    public void persistBackFile(BackFile bf)
    {
        fileDAL.persistBackFile(bf);
    }

    public List<FrontFile> getFiles(String parentPath, String ownerId)
    {
        try
        {
            List<String> dirAndOwner = getRealPathAndOwner(parentPath, ownerId);
            if(dirAndOwner.size() == 2)
            {
                ownerId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
            }
            parentPath = dirAndOwner.get(0);

        }
        catch ( FileNotFoundException e)
        {
            //use origin path
        }
        return fileDAL.getFiles(parentPath, ownerId);
    }

    public List<FrontFile> getAll(String parentPath, String ownerId)
    {
        try
        {
            List<String> dirAndOwner = getRealPathAndOwner(parentPath, ownerId);
            if(dirAndOwner.size() == 2)
            {
                ownerId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
            }
            parentPath = dirAndOwner.get(0);
        }
        catch (FileNotFoundException e)
        {
            //use origin path
        }
        return fileDAL.getAll(parentPath, ownerId);
    }

    public boolean folderExist(String path, String ownerId)
    {
        try {
            List<String> dirAndOwner = getRealPathAndOwner(path, ownerId);
                if(dirAndOwner.size() == 2)
            {
                ownerId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
            }
            path = dirAndOwner.get(0);
        }
        catch (FileNotFoundException e)
        {
            //use origin path
        }
        return fileDAL.folderExist(path, ownerId);
    }

    public boolean fileExist(String path, String name, String ownerId)
    {
        try
        {
            List<String> dirAndOwner = getRealPathAndOwner(path, ownerId);
            if(dirAndOwner.size() == 2)
            {
                ownerId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
            }
            path = dirAndOwner.get(0);
        }
        catch(FileNotFoundException e)
        {
            //use origin path
        }
        return fileDAL.fileExist(path, name, ownerId, false);
    }

    public String getAbsolutePath(String path, String currentPath)
    {
        //cd /
        if(path.equals("/"))
        {
            return path;
        }

        //relative
        if(!path.substring(0,1).equals("/"))
        {
            path = currentPath + path;
        }

        //delete / at the end
        int length = path.length();
        if(path.substring(length-1,length).equals("/"))
        {
            path = path.substring(0, length-1);
            length--;
        }

        //path without first /
        String modifPath = path.substring(1, length);
        String arborescence[] = modifPath.split("/");
        if(arborescence.length == 0)
            arborescence[0] = modifPath;

        ArrayList<String> newArborescence = new ArrayList<String>();
        int i = 0;

        for(String current : arborescence)
        {
            if(current.equals(".."))
            {
                --i;
            }
            else
            {
                newArborescence.add(i, current);
                System.out.println(current + " " + i);
                ++i;
            }

            if(i<0)
                return null;
        }

        path="";
        //case '/'
        if(newArborescence.isEmpty())
            path="/";

        //recreate path
        for(int j = 0 ; j < i ; ++j)
        {
            path += "/"+newArborescence.get(j);
        }

        path += "/";
        return path;
    }

    public int createDir(String folder, String dirName, String userId, boolean root)
    {
        List<String> dirAndOwner;

        if(isReadOnlyFolder(folder) && !root)
            return -2;

        try
        {
            dirAndOwner = getRealPathAndOwner(dirName, userId);
        }
        catch (FileNotFoundException e)
        {
            return -1;
        }

        if(dirAndOwner.size() == 2)
        {
            userId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
        }
        if(folderExist(folder+dirName+"/", userId))
            return -1;

        BackFile backFile = new BackFile();
        FrontFile newFrontFile = new FrontFile();

        backFile.setName(dirName);
        backFile.setSize(0);

        fileDAL.persistBackFile(backFile);

        newFrontFile.setAbsPath(folder);
        newFrontFile.setUser(userDAL.getUserById(userId));
        newFrontFile.setBackFile(backFile);

        fileDAL.persistFrontFile(newFrontFile);
        return 0;
    }

    public List<FrontFile> search(String nameToSearch, String searchDir, String userId)
    {
        try
        {
            List<String> dirAndOwner = getRealPathAndOwner(searchDir, userId);
            if(dirAndOwner.size() == 2)
            {
                userId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
            }
            searchDir = dirAndOwner.get(0);
        }
        catch(FileNotFoundException e)
        {
            //use parrent path
        }
        return fileDAL.search(nameToSearch, searchDir, userId);
    }

    public int share(String currentDir, String name, String userLogin, String ownerId)
    {
        List<String> dirAndOwner;
        User user = userDAL.getUserByLogin(userLogin);
        if(user == null)
            return -2;

        try
        {
            dirAndOwner = getRealPathAndOwner(currentDir, ownerId);
        }
        catch(FileNotFoundException e)
        {
            return -1;
        }
        if(dirAndOwner.size() > 1)
            return -4;

        boolean success = false;

        if(fileExist(currentDir, name, ownerId))
        {
            System.out.println("share file");
            /// Partage d'un fichier
            success = shareFile(currentDir, name, user, userDAL.getUserById(ownerId));
        }
        else if(folderExist(currentDir + name + "/", ownerId))
        {
            System.out.println("share folder");
            /// Partage d'un dossier
            success = shareFolder(currentDir, name, user, userDAL.getUserById(ownerId));
        }
        else
        {
            System.out.println("share error");
            return -1;
        }
        return success?0:-3;
    }

    public int move(String currentDir, String name, String newLocation, String ownerId)
    {
        newLocation = getAbsolutePath(newLocation, currentDir);
        if(isReadOnlyFolder(newLocation) || isReadOnlyFolder(currentDir))
            return -3;

        if(!folderExist(newLocation, ownerId))
            return -1;

        if(!folderExist(currentDir + name + "/", ownerId) && !fileExist(currentDir, name, ownerId))
            return -2;

        FrontFile frontFile = fileDAL.getFile(currentDir, name, ownerId);

        if(frontFile == null)
            return -3;

        frontFile.setAbsPath(newLocation);
        persistFrontFile(frontFile);

        return 0;
    }

    private static int nthOccurrence(String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(c, pos+1);
        return pos;
    }

    private boolean shareFile(String currentDir, String name, User userTarget, User owner)
    {
        String fileLocation = "/share/" + owner.getLogin() + "/files/";
        if(fileExist(fileLocation, name, owner.getIdUser()))
            return false;

        if(!folderExist("/share/" + owner.getLogin() + "/", userTarget.getIdUser()))
            createDir("/share/", owner.getLogin(), userTarget.getIdUser(), true);

        if(!folderExist(fileLocation, userTarget.getIdUser()))
            createDir("/share/" + owner.getLogin() + "/", "files", userTarget.getIdUser(), true);

        /// Create link to the file
        FrontFile fileRef = fileDAL.getFile(currentDir, name, owner.getIdUser());
        BackFile backFileRef = fileRef.getBackFile();

        FrontFile shortLink = new FrontFile();
        shortLink.setBackFile(backFileRef);
        shortLink.setAbsPath(fileLocation);
        shortLink.setUser(userTarget);

        fileDAL.saveFrontFile(shortLink);

        return true;
    }

    private boolean shareFolder(String currentDir, String name, User userTarget, User owner)
    {
        String fileLocation = "/share/" + owner.getLogin() + "/" + name + "/";
        if(folderExist(fileLocation, owner.getIdUser()))
        {
            return false;
        }

        if(!folderExist("/share/" + owner.getLogin() + "/", userTarget.getIdUser()))
            createDir("/share/", owner.getLogin(), userTarget.getIdUser(), true);

        if(!folderExist(fileLocation, userTarget.getIdUser()))
            createDir("/share/" + owner.getLogin() + "/", name, userTarget.getIdUser(), true);

        /// Create link to the external folder
        FrontFile shortLink = new FrontFile();
        shortLink.setAbsPath(fileLocation);
        shortLink.setUser(userTarget);
        shortLink.setSharePath(currentDir + name + "/");

        fileDAL.saveFrontFile(shortLink);

        return true;
    }

    private boolean isReadOnlyFolder(String path)
    {
        if(path.equals("/"))
            return true;
        if(path.startsWith("/share/"))
        {
            return path.split("/").length < 4;
        }
        return false;
    }

    private List<String> getRealPathAndOwner(String path, String idCurrentUser) throws FileNotFoundException {
        List<String> pathAndOwner = new ArrayList<String>();
        String[] splittedPath = path.split("/");
        if(path.startsWith("/share/") && splittedPath.length > 3 && !splittedPath[3].equals("files"))
        {
            System.out.println(path);
            int index = nthOccurrence(path, '/', 4);
            if(index != -1)
            {
                String link = path.substring(0, index + 1);
                String additionalPath = path.substring(index + 1);
                /// add the user
                String login = path.split("/")[2];
                pathAndOwner.add(login);

                System.out.println(link);
                System.out.println(login);
                FrontFile linkFile = fileDAL.getFileSymlink(link, idCurrentUser);

                if(linkFile == null)
                    throw new FileNotFoundException();

                if(linkFile.getSharePath() == null)
                {
                    throw new InvalidParameterException();
                }

                path = linkFile.getSharePath() + additionalPath;
                System.out.println(path);
            }
        }
        pathAndOwner.add(0, path);

        return pathAndOwner;
    }

    public FrontFile getFile(String path, String name, String ownerId)
    {
        if(fileDAL.fileExist(path, name, ownerId, false))
        {
            return fileDAL.getFile(path, name, ownerId);
        }

        return null;
    }

    public FrontFile getFolder(String path, String name, String ownerId)
    {
        if(fileDAL.folderExist(path + name + "/", ownerId))
        {
            return fileDAL.getFile(path, name, ownerId);
        }

        return null;
    }

    public boolean remove(String currentDir, String name, String userId)
    {
        if(fileExist(currentDir, name, userId) )
        {
            FrontFile frontFile = getFile(currentDir, name, userId);
            fileDAL.remove(frontFile);
            return true;
        }
        else if(folderExist(currentDir+name+"/", userId) && !isReadOnlyFolder(currentDir+name+"/"))
        {
            FrontFile frontFile = getFolder(currentDir, name, userId);
            fileDAL.remove(frontFile);
            return true;
        }
        else
        {
            System.out.println("file " + currentDir + " " + name + " " + userId + " doesn't exist.");
            return false;
        }
    }

    public int copy(String currentDir, String name, String newLocation, String ownerId)
    {
        newLocation = getAbsolutePath(newLocation, currentDir);
        if(isReadOnlyFolder(newLocation) || isReadOnlyFolder(currentDir))
            return -3;

        if(!folderExist(newLocation, ownerId))
            return -1;

        if(!folderExist(currentDir + name + "/", ownerId) && !fileExist(currentDir, name, ownerId))
            return -2;

        FrontFile frontFile = fileDAL.getFile(currentDir, name, ownerId);

        if(frontFile == null)
            return -3;

        FrontFile newFrontFile = new FrontFile();
        newFrontFile.setSharePath(frontFile.getSharePath());
        newFrontFile.setBackFile(frontFile.getBackFile());
        newFrontFile.setUser(frontFile.getUser());
        newFrontFile.setAbsPath(newLocation);
        persistFrontFile(newFrontFile);

        return 0;
    }
}
