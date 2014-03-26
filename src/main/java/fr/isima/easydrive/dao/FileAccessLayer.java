package fr.isima.easydrive.dao;

import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.FrontFile;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
