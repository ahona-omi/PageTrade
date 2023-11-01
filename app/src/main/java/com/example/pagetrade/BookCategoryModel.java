package com.example.pagetrade;

public class BookCategoryModel {
    private String categoryKey, title, image;

    public BookCategoryModel() {
    }
    public BookCategoryModel(String categoryKey, String title, String image) {
        this.categoryKey = categoryKey;
        this.title = title;
        this.image = image;
    }

    public String getCategoryKey() {
        return categoryKey;
    }
    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
