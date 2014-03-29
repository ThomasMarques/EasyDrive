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
        session.persist(frontFile);
        transaction.commit();
        session.close();
    }

    public void persistBackFile(BackFile backFile)
    {
        Session session = HibernateSession.getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(backFile);
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

    public FrontFile getFile(String parentPath, String name, String ownerId)
    {
        List<FrontFile> files =  getFiles(parentPath, ownerId);

        for(FrontFile file : files)
        {
            System.out.print(file.getBackFile().getName());
            if(file.getBackFile().getName().equals(name))
                return file;
        }

        return null;
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
        String name = path.substring( index+1, length-1 );

        return fileExist(absolutePath, name, ownerId, true);
    }

    public boolean fileExist(String path, String name, String ownerId, boolean isFolder)
    {
        List<FrontFile> files = getFiles(path, ownerId);
        for(FrontFile file : files)
        {
            if(file.getBackFile().getName().equals(name))
            {
                if( isFolder )
                {
                    if(file.getBackFile().getData() == null)
                    {
                        return true;
                    }
                    else
                        return false;
                }
                else
                {
                    if(file.getBackFile().getData() != null)
                    {
                        return true;
                    }
                    else
                        return false;
                }
            }
        }

        return false;
    }

    public List<FrontFile> search(String nameToSearch, String currentDir)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.Search");
        query.setString("nameToSearch", "%"+nameToSearch+"%");
        query.setString("path", currentDir+"%");
        List<FrontFile> files =  query.list();

        session.close();

        return files;
    }
}
