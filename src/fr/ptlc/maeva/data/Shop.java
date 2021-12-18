package fr.ptlc.maeva.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shop {
	
	private List<Article> articles = new ArrayList<Article>();
	
	public Shop() {
		
	}
	
	public Shop(List<Article> articles) {
		this.articles = articles;
	}
	
	public List<Article> getArticles() {
		return articles != null ? Collections.unmodifiableList(articles) : new ArrayList<Article>();
	}

	public Article getArticleById(int id) {
		for (int i = 0; i < articles.size(); i++)
			if (i == id)
				return this.articles.get(i);
		return null;
	}

	public void addArticle(Article article) {
		articles.add(article);
	}

	public void removeArticle(int indexOfArticle) {
		articles.remove(indexOfArticle);
	}
	
	public String toString() {
		return "[" + articles.toString() + "]";
	}
	
}
