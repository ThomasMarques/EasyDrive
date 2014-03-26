package fr.isima.easydrive.dao;

import fr.isima.easydrive.entity.BackFile;
import fr.isima.easydrive.entity.FrontFile;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.isima.easydrive.entity.User;

import java.security.InvalidParameterException;

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
}
