package com.getcloudcherry.survey.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 7/30/16.
 */
public class SurveyQuestions implements Parcelable {
    public String id;
    public String user;
    public String setName;
    public int sequence;
    public String text;
    public String audio;
    public String displayType;
    public ArrayList<String> multiSelect;
    public ArrayList<String> multiSelectChoiceTag;
    public boolean staffFill;
    public boolean apiFill;
    public boolean endOfSurvey;
    public String endOfSurveyMessage;
    public boolean isRequired;
    public ArrayList<String> questionTags;
    public String goodAfter;
    public String goodBefore;
    public String backgroundURL;
    public String disclaimerText;
    public String validationRegex;
    public String validationHint;
    public boolean isRetired;


    public SurveyQuestions(String id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return "SurveyQuestions{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.user);
        dest.writeString(this.setName);
        dest.writeInt(this.sequence);
        dest.writeString(this.text);
        dest.writeString(this.audio);
        dest.writeString(this.displayType);
        dest.writeStringList(this.multiSelect);
        dest.writeStringList(this.multiSelectChoiceTag);
        dest.writeByte(this.staffFill ? (byte) 1 : (byte) 0);
        dest.writeByte(this.endOfSurvey ? (byte) 1 : (byte) 0);
        dest.writeString(this.endOfSurveyMessage);
        dest.writeByte(this.isRequired ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.questionTags);
        dest.writeString(this.goodAfter);
        dest.writeString(this.goodBefore);
        dest.writeString(this.backgroundURL);
        dest.writeString(this.disclaimerText);
        dest.writeString(this.validationRegex);
        dest.writeString(this.validationHint);
        dest.writeByte(this.isRetired ? (byte) 1 : (byte) 0);
    }

    public SurveyQuestions() {
    }

    protected SurveyQuestions(Parcel in) {
        this.id = in.readString();
        this.user = in.readString();
        this.setName = in.readString();
        this.sequence = in.readInt();
        this.text = in.readString();
        this.audio = in.readString();
        this.displayType = in.readString();
        this.multiSelect = in.createStringArrayList();
        this.multiSelectChoiceTag = in.createStringArrayList();
        this.staffFill = in.readByte() != 0;
        this.endOfSurvey = in.readByte() != 0;
        this.endOfSurveyMessage = in.readString();
        this.isRequired = in.readByte() != 0;
        this.questionTags = in.createStringArrayList();
        this.goodAfter = in.readString();
        this.goodBefore = in.readString();
        this.backgroundURL = in.readString();
        this.disclaimerText = in.readString();
        this.validationRegex = in.readString();
        this.validationHint = in.readString();
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
