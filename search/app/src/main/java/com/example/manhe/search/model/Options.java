package com.example.manhe.search.model;

public class Options  {
    private int icon;
    private String name;
    private boolean ischecked;

    public Options(int icon, String name, boolean ischecked) {
        this.icon = icon;
        this.name = name;
        this.ischecked = ischecked;
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
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
