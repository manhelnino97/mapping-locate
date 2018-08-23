package com.example.manhe.search.model;

public class Service {
    private int id;
    private int icon;
    private String name;
    private Boolean ischecked;


    public Service(int id, int icon, String name, Boolean ischecked) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.ischecked = ischecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIschecked() {
        return ischecked;
    }

    public void setIschecked(Boolean ischecked) {
        this.ischecked = ischecked;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
