package com.main.java;

import java.util.ArrayList;
import java.util.List;

public class Category implements Comparable<Category> {

	private String title;
	private Category parentCategory;

	public Category(String title) {
		this(title, null);
	}

	public Category(String title, Category parentCategory) {
		this.title = title;
		this.parentCategory = parentCategory;
	}

	public boolean inCategory(Category category) {
		return category == this || (parentCategory != null && parentCategory.inCategory(category));
	}

	public List<Category> getAllParents() {
		List<Category> categories = new ArrayList<>();
		Category category = this;
		while (category != null) {
			categories.add(category);
			category = category.parentCategory;
		}
		return categories;
	}

	@Override
	public String toString() {
		return (parentCategory != null ? parentCategory.toString() + "\n   - " : "") + title;
	}

	@Override
	public int compareTo(Category category) {
		return this.toString().compareTo(category.toString());
	}
}