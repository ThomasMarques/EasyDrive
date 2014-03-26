package fr.isima.easydrive.dao;

import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.FrontFile;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.isima.easydrive.entity.User;

import java.security.InvalidParameterException;
import java.util.List;

public class FileAccessLayer {
    public void persistFrontFile(FrontFile ff)
    {
        Session session = HibernateSession.getSession();
        session.persist(ff);
    }

    public void persistBackFile(BackFile bf)
    {
        Session session = HibernateSession.getSession();
        session.persist(bf);
    }

    public List<FrontFile> getFile(String parentPath)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.findFileByParent");
        query.setString("parent", parentPath);
        List<FrontFile> files =  query.list();

        session.close();

        return files;
    }

    public List<FrontFile> getFolder(String parentPath)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.findFolderByParent");
        query.setString("parent", parentPath);
        List<FrontFile> files =  query.list();

        session.close();

        return files;
    }

    public List<FrontFile> getAll(String parentPath)
    {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("FrontFile.findAllByParent");
        query.setString("parent", parentPath);
        List<FrontFile> files =  query.list();

        session.close();

        return files;
    }
}
