package fr.isima.easydrive.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the front_file database table.
 * 
 */
@Entity
@Table(name="front_file")
@NamedQuery(name="FrontFile.findAll", query="SELECT f FROM FrontFile f")
public class FrontFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_front_file")
	private String idFrontFile;

	@Column(name="abs_path")
	private String absPath;

	private byte share;

	//bi-directional many-to-one association to BackFile
	@ManyToOne
	@JoinColumn(name="id_back_file")
	private BackFile backFile;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_owner")
	private User user;

	public FrontFile() {
	}

	public String getIdFrontFile() {
		return this.idFrontFile;
	}

	public void setIdFrontFile(String idFrontFile) {
		this.idFrontFile = idFrontFile;
	}

	public String getAbsPath() {
		return this.absPath;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}

	public byte getShare() {
		return this.share;
	}

	public void setShare(byte share) {
		this.share = share;
	}

	public BackFile getBackFile() {
		return this.backFile;
	}

	public void setBackFile(BackFile backFile) {
		this.backFile = backFile;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}