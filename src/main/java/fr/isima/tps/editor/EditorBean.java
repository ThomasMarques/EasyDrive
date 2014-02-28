package fr.isima.tps.editor;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "editor")
public class EditorBean {

	private String value = "This editor shows that Thomas is the best.";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}