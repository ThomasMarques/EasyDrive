package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.isima.easydrive.dao.FileAccessLayer;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.BackFile;

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
}
