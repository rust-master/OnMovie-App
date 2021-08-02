package com.zaryab.omovie.Model;

public class MovieDataB {

    private String Name, Link, Image,Description,MenudId;

    public MovieDataB() {
    }

    public MovieDataB(String link, String name, String image, String description, String menudId) {
        Name = name;
        Image = image;
        Description = description;
        Link = link;
        MenudId = menudId;

    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


    public String getMenudId() {
        return MenudId;
    }

    public void setMenudId(String menudId) {
        MenudId = menudId;
    }
}
