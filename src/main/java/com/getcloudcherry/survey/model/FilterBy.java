package com.getcloudcherry.survey.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/23/16.
 */
public class FilterBy implements Parcelable {
    public ArrayList<String> location;
    public String afterdate;
    public String beforedate;
    ArrayList<FilterByQuestions> filterquestions;
    public boolean archived;
    public boolean withTickets;
    public String withTicketStatus;
    public boolean withNotes;
    public String notesWithAttachmentType;
    public String notesMediaTheme;
    public String notesMediaMood;
    public boolean onlyWithAttachments;
    public ArrayList<String> days;
    public String aftertime;
    public String beforetime;
    public TicketFilter ticket;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.location);
        dest.writeString(this.afterdate);
        dest.writeString(this.beforedate);
        dest.writeList(this.filterquestions);
        dest.writeByte(this.archived ? (byte) 1 : (byte) 0);
        dest.writeByte(this.withTickets ? (byte) 1 : (byte) 0);
        dest.writeString(this.withTicketStatus);
        dest.writeByte(this.withNotes ? (byte) 1 : (byte) 0);
        dest.writeString(this.notesWithAttachmentType);
        dest.writeString(this.notesMediaTheme);
        dest.writeString(this.notesMediaMood);
        dest.writeByte(this.onlyWithAttachments ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.days);
        dest.writeString(this.aftertime);
        dest.writeString(this.beforetime);
        dest.writeParcelable(this.ticket, flags);
    }

    public FilterBy() {
    }

    protected FilterBy(Parcel in) {
        this.location = in.createStringArrayList();
        this.afterdate = in.readString();
        this.beforedate = in.readString();
        this.filterquestions = new ArrayList<FilterByQuestions>();
        in.readList(this.filterquestions, FilterByQuestions.class.getClassLoader());
        this.archived = in.readByte() != 0;
        this.withTickets = in.readByte() != 0;
        this.withTicketStatus = in.readString();
        this.withNotes = in.readByte() != 0;
        this.notesWithAttachmentType = in.readString();
        this.notesMediaTheme = in.readString();
        this.notesMediaMood = in.readString();
        this.onlyWithAttachments = in.readByte() != 0;
        this.days = in.createStringArrayList();
        this.aftertime = in.readString();
        this.beforetime = in.readString();
        this.ticket = in.readParcelable(TicketFilter.class.getClassLoader());
    }

    public static final Parcelable.Creator<FilterBy> CREATOR = new Parcelable.Creator<FilterBy>() {
        @Override
        public FilterBy createFromParcel(Parcel source) {
            return new FilterBy(source);
        }

        @Override
        public FilterBy[] newArray(int size) {
            return new FilterBy[size];
        }
    };
}
