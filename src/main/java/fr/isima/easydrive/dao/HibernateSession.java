package fr.isima.easydrive.dao;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.classic.Session;

public class HibernateSession {

    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory() {
    	try {
    		return new Configuration().configure().buildSessionFactory();
    	} catch (Throwable ex) {
    		System.err.println("Initial SessionFactory creation failed." + ex);
    		throw new ExceptionInInitializerError(ex);
    	}
    }
    
    public static Session getSession() {
    	return sessionFactory.openSession();    	
    }
}
