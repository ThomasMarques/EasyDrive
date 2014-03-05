package fr.isima.easydrive.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the back_file database table.
 * 
 */
@Entity
@Table(name="back_file")
@NamedQuery(name="BackFile.findAll", query="SELECT b FROM BackFile b")
public class BackFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="creation_date")
	private Date creationDate;

	@Lob
	private byte[] data;

	private String hash;

	@Temporal(TemporalType.DATE)
	@Column(name="last_modification_date")
	private Date lastModificationDate;

	private String name;

	private int size;

	//bi-directional many-to-one association to FrontFile
	@OneToMany(mappedBy="backFile")
	private List<FrontFile> frontFiles;

	public BackFile() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Date getLastModificationDate() {
		return this.lastModificationDate;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<FrontFile> getFrontFiles() {
		return this.frontFiles;
	}

	public void setFrontFiles(List<FrontFile> frontFiles) {
		this.frontFiles = frontFiles;
	}

	public FrontFile addFrontFile(FrontFile frontFile) {
		getFrontFiles().add(frontFile);
		frontFile.setBackFile(this);

		return frontFile;
	}

	public FrontFile removeFrontFile(FrontFile frontFile) {
		getFrontFiles().remove(frontFile);
		frontFile.setBackFile(null);

		return frontFile;
	}

}