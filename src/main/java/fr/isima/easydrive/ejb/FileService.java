package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.isima.easydrive.dao.FileAccessLayer;
import fr.isima.easydrive.entity.FrontFile;
import fr.isima.easydrive.entity.BackFile;

@Singleton
@LocalBean
public class FileService {

	private FileAccessLayer fileDAL;
	
	public FileService() {
		fileDAL = new FileAccessLayer();
	}
	
}
