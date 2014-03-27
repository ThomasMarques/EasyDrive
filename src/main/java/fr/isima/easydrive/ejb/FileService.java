package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.isima.easydrive.dao.FileAccessLayer;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.BackFile;

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
        return fileDAL.getFiles(parentPath, ownerId);
    }

    public List<FrontFile> getAll(String parentPath, String ownerId)
    {
        return fileDAL.getAll(parentPath, ownerId);
    }

    public boolean folderExist(String path, String ownerId)
    {
        return fileDAL.folderExist(path, ownerId);
    }

    public boolean fileExist(String path, String name, String ownerId)
    {
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
}
