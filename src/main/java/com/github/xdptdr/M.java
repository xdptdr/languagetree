package com.github.xdptdr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.impl.SimpleLog;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class M {

	private static final SimpleLog LOG = new SimpleLog(M.class.getSimpleName());
	static {
		LOG.setLevel(SimpleLog.LOG_LEVEL_ALL);
	}

	private static final int INCREMENT = 80;

	public static void main(String[] args) throws IOException {

		for (char ch = 'A'; ch < 'C'; ++ch) {

			int page = 0;

			List<String> newLinks = new ArrayList<>();
			do {
				newLinks.clear();
				Document doc = get80Entries(
						"http://www.cnrtl.fr/portailindex/LEXI/TLFI/" + ch + "/" + page * INCREMENT);
				Elements links = doc.select("a");
				links.forEach(element -> {
					if (element.attr("href").contains("/definition")) {
						newLinks.add(element.attr("href"));
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LOG.warn(e);
					Thread.currentThread().interrupt();
				}
				++page;
			} while (!newLinks.isEmpty() && page < 2);

		}
	}

	public static Document get80Entries(String url) throws IOException {

		LOG.info("Getting " + url);
		return Request.Get(url).execute().handleResponse(new ResponseHandler<Document>() {
			public Document handleResponse(final HttpResponse response) throws IOException {
				return Jsoup.parse(response.getEntity().getContent(), "UTF-8", "/");
			}
		});
	}
}
