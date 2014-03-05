package fr.isima.easydrive.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	private String login;

	private String password;

	private String salt;

	//bi-directional many-to-one association to FrontFile
	@OneToMany(mappedBy="user")
	private List<FrontFile> frontFiles;

	public User() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public List<FrontFile> getFrontFiles() {
		return this.frontFiles;
	}

	public void setFrontFiles(List<FrontFile> frontFiles) {
		this.frontFiles = frontFiles;
	}

	public FrontFile addFrontFile(FrontFile frontFile) {
		getFrontFiles().add(frontFile);
		frontFile.setUser(this);

		return frontFile;
	}

	public FrontFile removeFrontFile(FrontFile frontFile) {
		getFrontFiles().remove(frontFile);
		frontFile.setUser(null);

		return frontFile;
	}

}