package com.example.manhe.search.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    private String id;
    private String url;
    private String utilities_id;
    private Bitmap bitmap;
    public boolean check;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }



    public Image(String id, String url, String utilities_id,Bitmap bitmap,boolean check) {
        this.id = id;
        this.url = url;
        this.utilities_id = utilities_id;
        this.bitmap=bitmap;
        this.check=check;
    }


    private Image(Parcel in) {
        id = in.readString();
        url = in.readString();
        utilities_id = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUtilities_id() {
        return utilities_id;
    }

    public void setUtilities_id(String utilities_id) {
        this.utilities_id = utilities_id;
    }



    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(utilities_id);
    }
}
