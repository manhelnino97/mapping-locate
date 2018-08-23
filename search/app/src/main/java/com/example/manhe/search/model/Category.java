package com.example.manhe.search.model;

public class Category {
    private int icon;
    private   String id,slug,name,name_vi,name_en,name_cn,name_es,name_ko,type;

    public Category(int icon , String id, String slug, String name, String name_vi, String name_en, String name_cn, String name_es, String name_ko, String type){
        this.icon =icon;
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.name_vi = name_vi;
        this.name_en = name_en;
        this.name_cn = name_cn;
        this.name_es = name_es;
        this.name_ko = name_ko;
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_vi() {
        return name_vi;
    }

    public void setName_vi(String name_vi) {
        this.name_vi = name_vi;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public String getName_es() {
        return name_es;
    }

    public void setName_es(String name_es) {
        this.name_es = name_es;
    }

    public String getname_ko() {
        return name_ko;
    }

    public void setname_ko(String name_ko) {
        this.name_ko = name_ko;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
