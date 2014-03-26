package fr.isima.easydrive.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import fr.isima.easydrive.entity.User;

public class UserAccessLayer {
	
	public User getUser (Long userId) {
        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("User.findById");
        query.setLong("idUser", userId);

        User user = (User) query.uniqueResult();

        session.close();

        return user;
    }
	
	public User getUser (String login) {

        Session session = HibernateSession.getSession();
        Query query = session.getNamedQuery("User.findByLogin");
        query.setString("login", login);

        User user = (User) query.uniqueResult();

        session.close();

        return user;
    }
	
}
