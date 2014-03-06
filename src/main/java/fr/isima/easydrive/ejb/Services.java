package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.isima.easydrive.dao.UserAccessLayer;
import fr.isima.easydrive.entity.User;

@Singleton
@LocalBean
public class Services {

	private UserAccessLayer userDAL;
	
	public Services() {
		userDAL = new UserAccessLayer();
	}
	
	public User getUser (long userId) {
        return userDAL.getUser(userId);
    }
	
	public User getUser (String login) {
		return userDAL.getUser(login);
    }
	
}
