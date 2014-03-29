package fr.isima.easydrive.dao;

import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.FrontFile;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.Transaction;

import java.util.List;

public class FileAccessLayer {

    public void persistFrontFile(FrontFile frontFile)
    {
        Session session = HibernateSession.getSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(frontFile);
        transaction.commit();
        session.close();
    }

    public void persistBackFile(BackFile backFile)
    {
        Session session = HibernateSession.getSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(backFile);
        transaction.commit();
        session.close();
    }

    public void saveFrontFile(FrontFile frontFile)
    {
        Session session = HibernateSession.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(frontFile);
        transaction.commit();
        session.close();
    }

    public void saveBackFile(BackFile backFile)
    {
        Session session = HibernateSession.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(backFile);
        transaction.commit();
        session.close();
    }

    public List<FrontFile> getFiles(String parentPath, String ownerId)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.findFileByParent");
        query.setString("parent", parentPath);
        query.setString("owner_id", ownerId);
        List<FrontFile> files =  query.list();

        session.close();

        return files;
    }

   public FrontFile getFile(String path, String name, String ownerId)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.findFileByName");
        query.setString("path", path);
        query.setString("name", name);
        query.setString("owner_id", ownerId);
        FrontFile file = (FrontFile)query.uniqueResult();

        session.close();

        return file;
    }

    public FrontFile getFileSymlink(String path, String ownerId)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.findFileSymlink");
        query.setString("path", path);
        query.setString("owner_id", ownerId);
        FrontFile file = (FrontFile)query.uniqueResult();

        session.close();

        return file;
    }

    public List<FrontFile> getAll(String parentPath, String ownerId)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.findAllByParent");
        query.setString("parent", parentPath);
        query.setString("owner_id", ownerId);
        List<FrontFile> files =  query.list();

        session.close();

        return files;
    }

    public boolean folderExist(String path, String ownerId)
    {
        int length = path.length();
        int index = path.substring(0, length-1).lastIndexOf('/');
        String absolutePath = path.substring( 0, index+1 );
        String name = path.substring(index + 1, length - 1);

        return fileExist(absolutePath, name, ownerId, true);
    }

    public boolean fileExist(String path, String name, String ownerId, boolean isFolder)
    {
        List<FrontFile> files = getFiles(path, ownerId);
        for(FrontFile file : files)
        {
            if(file.getBackFile().getName().equals(name))
            {
                return file.isDirectory() == isFolder;
            }
        }

        return false;
    }

    public List<FrontFile> search(String nameToSearch, String currentDir, String userId)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.Search");
        query.setString("nameToSearch", "%"+nameToSearch+"%");
        query.setString("path", currentDir+"%");
        query.setString("userId", userId);
        List<FrontFile> files =  query.list();

        session.close();

        return files;
    }
}
