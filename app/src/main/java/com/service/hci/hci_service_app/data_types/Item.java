package com.service.hci.hci_service_app.data_types;

import android.media.Image;
import android.widget.ImageView;

import com.service.hci.hci_service_app.R;

public class Item {
    private Long id;
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

    public Item(Long id, String description, String name, String picture) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.picture = this.getPictureID(picture);
    }

    private int getPictureID(String pictureName) {
        switch (pictureName) {
            case "cola":
                return R.drawable.cola;
            case "beer":
                return R.drawable.beer;
            case "bratwurst":
                return R.drawable.bratwurst;
            case "brezel":
                return R.drawable.brezel;
            case "fanta":
                return R.drawable.fanta;
            case "hotdog":
                return R.drawable.hotdog;
            case "sprite":
                return R.drawable.sprite;
            default:
                return R.drawable.water;
        }
    }
}
