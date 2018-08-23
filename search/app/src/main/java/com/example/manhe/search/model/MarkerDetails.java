package com.example.manhe.search.model;

public class MarkerDetails {

    private String lat,lng,name,name_vi,name_en,add,add_vi,add_en,cate,id_d;
    private String name_es,name_cn,name_ko,add_es,add_cn,add_ko;
    private int id;

    public MarkerDetails(){};
    public MarkerDetails(String cate , String lat, String lng,
                         String name, String name_vi, String name_en, String name_es, String name_cn, String name_ko
                            , String add, String add_vi, String add_en , String add_es, String add_cn, String add_ko,
                         String id_d){
        this.cate = cate;
        this.lat=lat;
        this.lng=lng;
        this.name = name;
        this.name_vi = name_vi;
        this.name_en=name_en;
        this.name_es = name_es;
        this.name_cn=name_cn;
        this.name_ko = name_ko;
        this.add=add;
        this.add_vi=add_vi;
        this.add_en=add_en;
        this.add_es=add_es;
        this.add_cn=add_cn;
        this.add_ko=add_ko;
        this.id_d = id_d;
    }

    public String getName_es() {
        return name_es;
    }

    public void setName_es(String name_es) {
        this.name_es = name_es;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public String getname_ko() {
        return name_ko;
    }

    public void setname_ko(String name_ko) {
        this.name_ko = name_ko;
    }

    public String getAdd_es() {
        return add_es;
    }

    public void setAdd_es(String add_es) {
        this.add_es = add_es;
    }

    public String getAdd_cn() {
        return add_cn;
    }

    public void setAdd_cn(String add_cn) {
        this.add_cn = add_cn;
    }

    public String getadd_ko() {
        return add_ko;
    }

    public void setadd_ko(String add_ko) {
        this.add_ko = add_ko;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
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

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getAdd_vi() {
        return add_vi;
    }

    public void setAdd_vi(String add_vi) {
        this.add_vi = add_vi;
    }

    public String getAdd_en() {
        return add_en;
    }

    public void setAdd_en(String add_en) {
        this.add_en = add_en;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getId_d() {
        return id_d;
    }

    public void setId_d(String id_d) {
        this.id_d = id_d;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
