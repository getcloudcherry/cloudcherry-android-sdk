package com.getcloudcherry.survey.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 7/30/16.
 */
public class SurveyQuestions implements Parcelable {
    public String id;
    public String text;
    public String user;
    public String displayType;
    public ArrayList<String> multiSelect;
    public boolean isRequired;
    public int sequence;
    public boolean isRetired;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.text);
        dest.writeString(this.user);
        dest.writeString(this.displayType);
        dest.writeStringList(this.multiSelect);
        dest.writeByte(this.isRequired ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sequence);
        dest.writeByte(this.isRetired ? (byte) 1 : (byte) 0);
    }

    public SurveyQuestions() {
    }

    protected SurveyQuestions(Parcel in) {
        this.id = in.readString();
        this.text = in.readString();
        this.user = in.readString();
        this.displayType = in.readString();
        this.multiSelect = in.createStringArrayList();
        this.isRequired = in.readByte() != 0;
        this.sequence = in.readInt();
        this.isRetired = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SurveyQuestions> CREATOR = new Parcelable.Creator<SurveyQuestions>() {
        @Override
        public SurveyQuestions createFromParcel(Parcel source) {
            return new SurveyQuestions(source);
        }

        @Override
        public SurveyQuestions[] newArray(int size) {
            return new SurveyQuestions[size];
        }
    };
}
