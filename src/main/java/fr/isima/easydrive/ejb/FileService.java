package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.isima.easydrive.dao.FileAccessLayer;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.User;

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

    public List<FrontFile> getFile(String parentPath)
    {
        return fileDAL.getFile(parentPath);
    }

    public List<FrontFile> getFolder(String parentPath)
    {
        return fileDAL.getFolder(parentPath);
    }

    public List<FrontFile> getAll(String parentPath)
    {
        return fileDAL.getAll(parentPath);
    }

    public int createDir(String folder, String dirName, User user)
    {
        for(FrontFile frontFile : getAll(folder))
        {
            if(frontFile.getBackFile().getName().equals(dirName))
                return -1;
        }

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
}
