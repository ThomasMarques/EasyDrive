package fr.isima.easydrive.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.isima.easydrive.dao.UserAccessLayer;
import fr.isima.easydrive.entity.User;

import java.security.InvalidParameterException;

@Singleton
@LocalBean
public class UserService {

	private UserAccessLayer userDAL;
	
	public UserService() {
		userDAL = new UserAccessLayer();
	}
	
	public User getUserById(String userId) {
        return userDAL.getUser(userId);
    }
	
	public User getUserByLogin(String login) {
		return userDAL.getUser(login);
    }
	
}
