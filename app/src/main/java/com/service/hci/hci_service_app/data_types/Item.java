package com.service.hci.hci_service_app.data_types;

import android.media.Image;
import android.widget.ImageView;

public class Item {
    private String description;
    private String name;
    private int picture;

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

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public Item(String description, String name, int picture) {

        this.description = description;
        this.name = name;
        this.picture = picture;
    }
}
