import org.w3c.dom.Node;

public class Language {

	private Node href;
	private String name;

	public Language(Node href, String name) {
		this.href = href;
		this.name = name;
	}

	public Node getHref() {
		return href;
	}

	public void setHref(Node href) {
		this.href = href;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
