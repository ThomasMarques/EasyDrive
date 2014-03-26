package fr.isima.easydrive.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import fr.isima.easydrive.entity.User;

import java.security.InvalidParameterException;

public class UserAccessLayer {
	
	public User getUserByID (String userId) {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("User.findById");
        query.setString("idUser", userId);

        User user = (User) query.uniqueResult();

        session.close();

        if (user == null) {
            throw new InvalidParameterException("No user found with this id.");
        }

        return user;
    }
	
	public User getUserByLogin (String login) {

        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("User.findByLogin");
        query.setString("login", login);

        User user = (User) query.uniqueResult();

        session.close();

        if (user == null) {
            throw new InvalidParameterException("No user found with this login.");
        }

        return user;
    }
	
}
