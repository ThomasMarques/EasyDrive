package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import fr.isima.easydrive.dao.FileAccessLayer;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.User;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
@LocalBean
public class FileService {

	private FileAccessLayer fileDAL;
	
	public FileService() {
		fileDAL = new FileAccessLayer();
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
        parentPath = getRealPath(parentPath);
        return fileDAL.getFiles(parentPath, ownerId);
    }

    public List<FrontFile> getAll(String parentPath, String ownerId)
    {
        parentPath = getRealPath(parentPath);
        return fileDAL.getAll(parentPath, ownerId);
    }

    public boolean folderExist(String path, String ownerId)
    {
        path = getRealPath(path);
        return fileDAL.folderExist(path, ownerId);
    }

    public boolean fileExist(String path, String name, String ownerId)
    {
        path = getRealPath(path);
        return fileDAL.fileExist(path, name, ownerId, false);
    }

    public String getAbsolutePath(String path, String currentPath)
    {
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

    public String getRealPath(String path)
    {
        //TODO : get real
        return path;
    }

    public int createDir(String folder, String dirName, User user)
    {
        dirName = getRealPath(dirName);
        if(folderExist(folder+dirName+"/", user.getIdUser()))
            return -1;

        BackFile backFile = new BackFile();
        FrontFile newFrontFile = new FrontFile();

        backFile.setName(dirName);
        backFile.setSize(0);

        fileDAL.persistBackFile(backFile);

        newFrontFile.setAbsPath(folder);
        newFrontFile.setUser(user);
        newFrontFile.setBackFile(backFile);

        fileDAL.persistFrontFile(newFrontFile);
        return 0;
    }

    public List<FrontFile> search(String nameToSearch, String currentDir)
    {
        currentDir = getRealPath(currentDir);
        return fileDAL.search(nameToSearch, currentDir);
    }

    public FrontFile getFile(String path, String name, String ownerId)
    {
        System.out.println("recv");
        if(fileDAL.fileExist(path, name, ownerId, false))
        {
            System.out.println("FrontFile found");
            return fileDAL.getFile(path, name, ownerId);
        }
        System.out.println("FrontFile not found");

        return null;
    }
}
