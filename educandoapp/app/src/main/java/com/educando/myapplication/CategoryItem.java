package com.educando.myapplication;

public class CategoryItem {
    private String categoryName;
    private int imageResource; // Esta es la referencia a la imagen

    public CategoryItem(String categoryName, int imageResource) {
        this.categoryName = categoryName;
        this.imageResource = imageResource;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getImageResourceId() {
        return imageResource;
    }
}
