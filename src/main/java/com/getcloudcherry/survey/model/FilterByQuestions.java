package com.getcloudcherry.survey.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/23/16.
 */
public class FilterByQuestions implements Parcelable {
    public String questionId;
    public ArrayList<String> answerCheck;
    public Integer number;
    public String groupBy;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionId);
        dest.writeStringList(this.answerCheck);
        dest.writeValue(this.number);
        dest.writeString(this.groupBy);
    }

    public FilterByQuestions() {
    }

    protected FilterByQuestions(Parcel in) {
        this.questionId = in.readString();
        this.answerCheck = in.createStringArrayList();
        this.number = (Integer) in.readValue(Integer.class.getClassLoader());
        this.groupBy = in.readString();
    }

    public static final Parcelable.Creator<FilterByQuestions> CREATOR = new Parcelable.Creator<FilterByQuestions>() {
        @Override
        public FilterByQuestions createFromParcel(Parcel source) {
            return new FilterByQuestions(source);
        }

        @Override
        public FilterByQuestions[] newArray(int size) {
            return new FilterByQuestions[size];
        }
    };
}
