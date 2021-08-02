package com.zaryab.omovie.Model;

public class TrailersItem {
    private String name;
    private String Image;
    private String Link;

    String menuId;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public TrailersItem() {
    }

    public TrailersItem(String name, String image , String link ) {
        this.name = name;
        Image = image;
        Link = link;

    }



    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
