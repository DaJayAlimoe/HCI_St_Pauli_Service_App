package com.service.hci.hci_service_app.data_types;

public class Item {
    private String description;
    private String name;
    private String picture;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Item(String description, String name, String picture) {

        this.description = description;
        this.name = name;
        this.picture = picture;
    }
}
