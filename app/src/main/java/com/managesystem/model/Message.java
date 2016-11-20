package com.managesystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.managesystem.jpush.JPUSHModel;

/**
 * Created by puhua on 2016/11/10.
 *
 * @
 */

public class Message extends JPUSHModel implements Parcelable{

    public String ctime;
    public int messageId;
    public int status;
    public String title;
    public String userId;
    public Message(){}

    protected Message(Parcel in){
        ctime = in.readString();
        messageId = in.readInt();
        status = in.readInt();
        title = in.readString();
        userId = in.readString();
        rid = in.readString();
        type = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ctime);
        dest.writeInt(messageId);
        dest.writeString(userId);
        dest.writeInt(status);
        dest.writeString(title);
        dest.writeString(rid);
        dest.writeString(type);
        dest.writeString(content);
    }
    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
