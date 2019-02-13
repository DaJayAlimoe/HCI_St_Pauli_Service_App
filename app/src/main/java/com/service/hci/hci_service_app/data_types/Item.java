package com.service.hci.hci_service_app.data_types;

import android.media.Image;
import android.widget.ImageView;

import com.service.hci.hci_service_app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    public Long getId() {
        return id;
    }

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

    public Item(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.description = jsonObject.getString("description");
            this.name = jsonObject.getString("name");
            this.picture = this.getPictureID(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private int getPictureID(String pictureName) {
        switch (pictureName) {
            case "Cola":
                return R.drawable.cola;
            case "Bier":
                return R.drawable.beer;
            case "Bratwurst":
                return R.drawable.bratwurst;
            case "Breezel":
                return R.drawable.brezel;
            case "Fanta":
                return R.drawable.fanta;
//            case "Sprite":
//                return R.drawable.hotdog;
            case "Sprite":
                return R.drawable.sprite;
            default:
                return R.drawable.water;
        }
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", picture=" + picture +
                '}';
    }
}
