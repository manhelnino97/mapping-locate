package com.example.manhe.search.model;

import java.util.ArrayList;

public class ConvenienceStore {

    private String cate,id_d,lat,lng;
    private String name,name_vi,name_en,name_es,name_cn,name_ko;
    private String memo,memo_vi,memo_en,memo_es,memo_cn,memo_ko;
    private String add,add_vi,add_en,add_es,add_cn,add_ko;

    private ArrayList<Image> imageArrayList;

    private ArrayList<Integer> listIdService,listIdPayment;

    private String phonnumber,senditems,receiveitems;
    private ArrayList<Integer> listTag;


    public ConvenienceStore(String cate, String id_d, String lat, String lng,
                            String name, String name_vi, String name_en, String name_es, String name_cn, String name_ko,
                            String memo, String memo_vi, String memo_en, String memo_es, String memo_cn, String memo_ko,
                            String add, String add_vi, String add_en, String add_es, String add_cn, String add_ko,
                            ArrayList<Image> imageArrayList, ArrayList<Integer> listIdService, ArrayList<Integer> listIdPayment,
                            String phonnumber, String senditems, String receiveitems, ArrayList<Integer> listTag) {
        this.cate = cate;
        this.id_d = id_d;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.name_vi = name_vi;
        this.name_en = name_en;
        this.name_es = name_es;
        this.name_cn = name_cn;
        this.name_ko = name_ko;
        this.memo = memo;
        this.memo_vi = memo_vi;
        this.memo_en = memo_en;
        this.memo_es = memo_es;
        this.memo_cn = memo_cn;
        this.memo_ko = memo_ko;
        this.add = add;
        this.add_vi = add_vi;
        this.add_en = add_en;
        this.add_es = add_es;
        this.add_cn = add_cn;
        this.add_ko = add_ko;
        this.imageArrayList = imageArrayList;
        this.listIdService = listIdService;
        this.listIdPayment = listIdPayment;
        this.phonnumber = phonnumber;
        this.senditems = senditems;
        this.receiveitems = receiveitems;
        this.listTag = listTag;
    }

    public ArrayList<Integer> getListTag() {
        return listTag;
    }

    public void setListTag(ArrayList<Integer> listTag) {
        this.listTag = listTag;
    }

    public String getSenditems() {
        return senditems;
    }

    public void setSenditems(String senditems) {
        this.senditems = senditems;
    }

    public String getReceiveitems() {
        return receiveitems;
    }

    public void setReceiveitems(String receiveitems) {
        this.receiveitems = receiveitems;
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

    public String getName_ko() {
        return name_ko;
    }

    public void setName_ko(String name_ko) {
        this.name_ko = name_ko;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo_vi() {
        return memo_vi;
    }

    public void setMemo_vi(String memo_vi) {
        this.memo_vi = memo_vi;
    }

    public String getMemo_en() {
        return memo_en;
    }

    public void setMemo_en(String memo_en) {
        this.memo_en = memo_en;
    }

    public String getMemo_es() {
        return memo_es;
    }

    public void setMemo_es(String memo_es) {
        this.memo_es = memo_es;
    }

    public String getMemo_cn() {
        return memo_cn;
    }

    public void setMemo_cn(String memo_cn) {
        this.memo_cn = memo_cn;
    }

    public String getMemo_ko() {
        return memo_ko;
    }

    public void setMemo_ko(String memo_ko) {
        this.memo_ko = memo_ko;
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

    public String getAdd_ko() {
        return add_ko;
    }

    public void setAdd_ko(String add_ko) {
        this.add_ko = add_ko;
    }

    public ArrayList<Image> getImageArrayList() {
        return imageArrayList;
    }

    public void setImageArrayList(ArrayList<Image> imageArrayList) {
        this.imageArrayList = imageArrayList;
    }

    public ArrayList<Integer> getListIdService() {
        return listIdService;
    }

    public void setListIdService(ArrayList<Integer> listIdService) {
        this.listIdService = listIdService;
    }

    public ArrayList<Integer> getListIdPayment() {
        return listIdPayment;
    }

    public void setListIdPayment(ArrayList<Integer> listIdPayment) {
        this.listIdPayment = listIdPayment;
    }

    public String getPhonnumber() {
        return phonnumber;
    }

    public void setPhonnumber(String phonnumber) {
        this.phonnumber = phonnumber;
    }
}
