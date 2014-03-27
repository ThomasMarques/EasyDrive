package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.isima.easydrive.dao.FileAccessLayer;
import fr.isima.easydrive.dao.UserAccessLayer;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.User;

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
        List<String> dirAndOwner = getRealPathAndOwner(parentPath);
        if(dirAndOwner.size() == 2)
        {
            ownerId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
        }
        parentPath = dirAndOwner.get(0);
        return fileDAL.getFiles(parentPath, ownerId);
    }

    public List<FrontFile> getAll(String parentPath, String ownerId)
    {
        List<String> dirAndOwner = getRealPathAndOwner(parentPath);
        if(dirAndOwner.size() == 2)
        {
            ownerId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
        }
        parentPath = dirAndOwner.get(0);
        return fileDAL.getAll(parentPath, ownerId);
    }

    public boolean folderExist(String path, String ownerId)
    {
        List<String> dirAndOwner = getRealPathAndOwner(path);
        if(dirAndOwner.size() == 2)
        {
            ownerId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
        }
        path = dirAndOwner.get(0);
        return fileDAL.folderExist(path, ownerId);
    }

    public boolean fileExist(String path, String name, String ownerId)
    {
        List<String> dirAndOwner = getRealPathAndOwner(path);
        if(dirAndOwner.size() == 2)
        {
            ownerId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
        }
        path = dirAndOwner.get(0);
        return fileDAL.fileExist(path, name, ownerId, false);
    }

    public String getAbsolutePath(String path, String currentPath)
    {
        //relative
        if(!path.substring(0,1).equals("/"))
        {
            path = currentPath + path;
            System.out.println("relative : " + path);
        }

        //delete / at the end
        int length = path.length();
        if(path.substring(length-1,length).equals("/"))
        {
            path = path.substring(0, length-1);
            length--;
            System.out.println("delete end / : " + path);
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

    public List<String> getRealPathAndOwner(String path)
    {
        List<String> pathAndOwner = new ArrayList<String>();
        if(path.startsWith("/share/"))
        {
            System.out.println(path);
            int index = nthOccurrence(path, '/', 4);
            if(index != -1)
            {
                String link = path.substring(0, index);
                System.out.println(link);
                String additionalPath = path.substring(index);
                System.out.println(additionalPath);
                /// add the user
                pathAndOwner.add(path.split("/")[1]);
                System.out.println(path.split("/")[1]);
            }
        }
        pathAndOwner.add(0, path);

        return pathAndOwner;
    }

    public int createDir(String folder, String dirName, String userId)
    {
        List<String> dirAndOwner = getRealPathAndOwner(dirName);
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

    public List<FrontFile> search(String nameToSearch, String currentDir, String userId)
    {
        List<String> dirAndOwner = getRealPathAndOwner(currentDir);
        if(dirAndOwner.size() == 2)
        {
            userId = userDAL.getUserByLogin(dirAndOwner.get(1)).getIdUser();
        }
        currentDir = dirAndOwner.get(0);
        return fileDAL.search(nameToSearch, currentDir, userId);
    }

    private static int nthOccurrence(String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(c, pos+1);
        return pos;
    }
}
