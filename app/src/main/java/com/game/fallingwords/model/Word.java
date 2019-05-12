package com.game.fallingwords.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Word implements Parcelable {

    @SerializedName("text_eng")
    private String textInEnglish;

    @SerializedName("text_spa")
    private String textInSpanish;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.textInEnglish);
        dest.writeString(this.textInSpanish);
    }

    public String getTextInSpanish() {
        return textInSpanish;
    }

    public String getTextInEnglish() {
        return textInEnglish;
    }

    protected Word(Parcel in) {
        this.textInEnglish = in.readString();
        this.textInSpanish = in.readString();
    }

    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel source) {
            return new Word(source);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };
}
