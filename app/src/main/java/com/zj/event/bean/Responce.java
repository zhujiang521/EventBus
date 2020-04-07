package com.zj.event.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Responce implements Parcelable {
    private String data;

    protected Responce(Parcel in) {
        data = in.readString();
    }

    public static final Creator<Responce> CREATOR = new Creator<Responce>() {
        @Override
        public Responce createFromParcel(Parcel in) {
            return new Responce(in);
        }

        @Override
        public Responce[] newArray(int size) {
            return new Responce[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
    }
}
