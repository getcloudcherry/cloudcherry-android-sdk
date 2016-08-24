package com.getcloudcherry.survey.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by riteshdubey on 8/23/16.
 */
public class TicketFilter implements Parcelable {
    public String status;
    public String assignedBy;
    public String action;
    public String department;
    public String language;
    public String orginalRoutedTo;
    public String nextEscalationUser;
    public String currentAssignedTo;
    public Integer priority;
    public boolean isEscalated;
    public boolean isDeferred;
    public boolean isPendingRating;
    public boolean isRated;
    public boolean isShowcased;
    public boolean isGlobalShowcased;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.assignedBy);
        dest.writeString(this.action);
        dest.writeString(this.department);
        dest.writeString(this.language);
        dest.writeString(this.orginalRoutedTo);
        dest.writeString(this.nextEscalationUser);
        dest.writeString(this.currentAssignedTo);
        dest.writeValue(this.priority);
        dest.writeByte(this.isEscalated ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDeferred ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPendingRating ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRated ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowcased ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isGlobalShowcased ? (byte) 1 : (byte) 0);
    }

    public TicketFilter() {
    }

    protected TicketFilter(Parcel in) {
        this.status = in.readString();
        this.assignedBy = in.readString();
        this.action = in.readString();
        this.department = in.readString();
        this.language = in.readString();
        this.orginalRoutedTo = in.readString();
        this.nextEscalationUser = in.readString();
        this.currentAssignedTo = in.readString();
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isEscalated = in.readByte() != 0;
        this.isDeferred = in.readByte() != 0;
        this.isPendingRating = in.readByte() != 0;
        this.isRated = in.readByte() != 0;
        this.isShowcased = in.readByte() != 0;
        this.isGlobalShowcased = in.readByte() != 0;
    }

    public static final Parcelable.Creator<TicketFilter> CREATOR = new Parcelable.Creator<TicketFilter>() {
        @Override
        public TicketFilter createFromParcel(Parcel source) {
            return new TicketFilter(source);
        }

        @Override
        public TicketFilter[] newArray(int size) {
            return new TicketFilter[size];
        }
    };
}
