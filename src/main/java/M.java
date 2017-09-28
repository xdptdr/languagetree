import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class M {

	public static void main(String[] args)
			throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {

		CloseableHttpClient client = HttpClients.createDefault();

		HttpGet request = new HttpGet("https://en.wikipedia.org/wiki/Speedcoding");
		CloseableHttpResponse response = client.execute(request);
		InputStream content = response.getEntity().getContent();

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(content, "/");

		Set<Language> influencedByLanguages = new HashSet<>();
		Set<Language> influencedLanguages = new HashSet<>();
		process(document, influencedByLanguages, influencedLanguages);
		
		for(Language language : influencedByLanguages) {
			
		}

		System.out.println("Hello world !");
	}

	private static void process(Node node, Set<Language> influencedByLanguages, Set<Language> influencedLanguages) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			Node child = childNodes.item(i);
			if (node.getNodeType() == Node.TEXT_NODE) {
				if ("Influenced by".equals(node.getNodeValue())) {
					addInfluencedBy(node.getParentNode().getParentNode().getNextSibling().getFirstChild(),
							influencedByLanguages);
				}
				if ("Influenced".equals(node.getNodeValue())) {
					addInfluenced(node.getParentNode().getParentNode().getNextSibling().getFirstChild(),
							influencedLanguages);
				}
			}
			process(child, influencedByLanguages, influencedLanguages);
		}
	}

	private static void addInfluenced(Node node, Set<Language> languages) {
		hreflist(node, languages);
	}

	private static void addInfluencedBy(Node node, Set<Language> languages) {
		hreflist(node, languages);
	}

	private static void hreflist(Node node, Set<Language> languages) {
		NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); ++i) {
			Node child = childs.item(i);
			if (child.getNodeName().toLowerCase().equals("a")) {
				Node href = child.getAttributes().getNamedItem("href");
				String name = child.getFirstChild().getNodeValue();
				Language l = new Language(href, name);
				languages.add(l);
			}
		}
	}
}
