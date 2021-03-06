package fr.isima.easydrive.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the front_file database table.
 * 
 */
@Entity
@Table(name="front_file")
@NamedQueries({
    @NamedQuery(name="FrontFile.findAll", query="SELECT f FROM FrontFile f "),
    @NamedQuery(name="FrontFile.findFileByParent", query="SELECT f FROM FrontFile f WHERE abs_path LIKE :parent AND id_owner = :owner_id"),
    @NamedQuery(name="FrontFile.findAllByParent", query="SELECT f FROM FrontFile f WHERE abs_path LIKE :parent AND id_owner = :owner_id"),
    @NamedQuery(name="FrontFile.findFileByName", query="SELECT f FROM FrontFile f WHERE abs_path LIKE :path AND id_owner = :owner_id AND f.backFile.name LIKE :name"),
    @NamedQuery(name="FrontFile.findFileSymlink", query="SELECT f FROM FrontFile f WHERE abs_path LIKE :path AND id_owner = :owner_id"),
    @NamedQuery(name="FrontFile.Search", query="SELECT f FROM FrontFile f WHERE f.backFile.name LIKE :nameToSearch AND abs_path LIKE :path AND id_owner = :userId")
})
public class FrontFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_front_file")
	private String idFrontFile;

	@Column(name="abs_path")
	private String absPath;

    @Column(name="share_path")
    private String sharePath;

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

    public String getSharePath() {
        return this.sharePath;
    }

    public void setSharePath(String sharePath) {
        this.sharePath = sharePath;
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

    public boolean isDirectory() {
        return getBackFile() != null && getBackFile().getData() == null;
    }
}