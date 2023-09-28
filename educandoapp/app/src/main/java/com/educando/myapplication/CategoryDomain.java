package com.educando.myapplication;

public class CategoryDomain {
    private String categoryName;
    private String imageResource; // Esta es la referencia a la imagen

    public CategoryDomain(String categoryName, String imageResource) {
        this.categoryName = categoryName;
        this.imageResource = imageResource;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageResourceId() {
        return imageResource;
    }

    public void setImageResourceId(String imageResource) {
        this.imageResource = imageResource;
    }
}
