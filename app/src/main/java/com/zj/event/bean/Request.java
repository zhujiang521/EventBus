package com.zj.event.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Request implements Parcelable {
    private String data;

    //反序列化
    protected Request(Parcel in) {
        data = in.readString();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //序列化
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
    }
}
