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
    public ArrayList<LeadingOption> leadingDisplayTexts;
    public ArrayList<String> displayLocation;
    public ArrayList<String> displayLocationByTag;
    public String displayStyle;
    public boolean endOfSurvey;
    public String endOfSurveyMessage;
    public FilterBy conditionalFilter;
    public String presentationMode;
    public boolean isRequired;
    public ArrayList<String> questionTags;
    public ArrayList<String> topicTags;
    public String goodAfter;
    public String goodBefore;
    public String timeOfDayAfter;
    public String timeOfDayBefore;
    public boolean isRetired;
    public String note;
    public String backgroundURL;
    public String disclaimerText;
    public String validationRegex;
    public String validationHint;
    public Integer timeLimit;
    public String interactiveLiveAPIPreFillUrl;
    public boolean restrictedData;


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


    @Override
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
        dest.writeByte(this.apiFill ? (byte) 1 : (byte) 0);
        dest.writeList(this.leadingDisplayTexts);
        dest.writeStringList(this.displayLocation);
        dest.writeStringList(this.displayLocationByTag);
        dest.writeString(this.displayStyle);
        dest.writeByte(this.endOfSurvey ? (byte) 1 : (byte) 0);
        dest.writeString(this.endOfSurveyMessage);
        dest.writeParcelable(this.conditionalFilter, flags);
        dest.writeString(this.presentationMode);
        dest.writeByte(this.isRequired ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.questionTags);
        dest.writeStringList(this.topicTags);
        dest.writeString(this.goodAfter);
        dest.writeString(this.goodBefore);
        dest.writeString(this.timeOfDayAfter);
        dest.writeString(this.timeOfDayBefore);
        dest.writeByte(this.isRetired ? (byte) 1 : (byte) 0);
        dest.writeString(this.note);
        dest.writeString(this.backgroundURL);
        dest.writeString(this.disclaimerText);
        dest.writeString(this.validationRegex);
        dest.writeString(this.validationHint);
        dest.writeValue(this.timeLimit);
        dest.writeString(this.interactiveLiveAPIPreFillUrl);
        dest.writeByte(this.restrictedData ? (byte) 1 : (byte) 0);
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
        this.apiFill = in.readByte() != 0;
        this.leadingDisplayTexts = new ArrayList<LeadingOption>();
        in.readList(this.leadingDisplayTexts, LeadingOption.class.getClassLoader());
        this.displayLocation = in.createStringArrayList();
        this.displayLocationByTag = in.createStringArrayList();
        this.displayStyle = in.readString();
        this.endOfSurvey = in.readByte() != 0;
        this.endOfSurveyMessage = in.readString();
        this.conditionalFilter = in.readParcelable(FilterBy.class.getClassLoader());
        this.presentationMode = in.readString();
        this.isRequired = in.readByte() != 0;
        this.questionTags = in.createStringArrayList();
        this.topicTags = in.createStringArrayList();
        this.goodAfter = in.readString();
        this.goodBefore = in.readString();
        this.timeOfDayAfter = in.readString();
        this.timeOfDayBefore = in.readString();
        this.isRetired = in.readByte() != 0;
        this.note = in.readString();
        this.backgroundURL = in.readString();
        this.disclaimerText = in.readString();
        this.validationRegex = in.readString();
        this.validationHint = in.readString();
        this.timeLimit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.interactiveLiveAPIPreFillUrl = in.readString();
        this.restrictedData = in.readByte() != 0;
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
