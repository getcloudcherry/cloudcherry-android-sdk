package com.getcloudcherry.survey.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/23/16.
 */
public class LeadingOption implements Parcelable {
    public String text;
    public String audio;
    public ArrayList<String> multiSelect;
    public FilterBy filter;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.audio);
        dest.writeStringList(this.multiSelect);
        dest.writeParcelable(this.filter, flags);
    }

    public LeadingOption() {
    }

    protected LeadingOption(Parcel in) {
        this.text = in.readString();
        this.audio = in.readString();
        this.multiSelect = in.createStringArrayList();
        this.filter = in.readParcelable(FilterBy.class.getClassLoader());
    }

    public static final Parcelable.Creator<LeadingOption> CREATOR = new Parcelable.Creator<LeadingOption>() {
        @Override
        public LeadingOption createFromParcel(Parcel source) {
            return new LeadingOption(source);
        }

        @Override
        public LeadingOption[] newArray(int size) {
            return new LeadingOption[size];
        }
    };
}
