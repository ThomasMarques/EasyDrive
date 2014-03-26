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
