package com.example.aplikacja3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProgressInfo implements Parcelable {
    public long mDownloadedBytes;
    public long mFileSize;
    public int mStatus;

    public long getmDownloadedBytes()
    {
        return mDownloadedBytes;
    }

    public long getmFileSize()
    {
        return mFileSize;
    }

    public int getmStatus()
    {
        return mStatus;
    }
    protected ProgressInfo(Parcel in) {
        mDownloadedBytes = in.readLong();
        mFileSize = in.readLong();
        mStatus = in.readInt();
    }

    public static final Creator<ProgressInfo> CREATOR = new Creator<ProgressInfo>() {
        @Override
        public ProgressInfo createFromParcel(Parcel in) {
            return new ProgressInfo(in);
        }

        @Override
        public ProgressInfo[] newArray(int size) {
            return new ProgressInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(mDownloadedBytes);
        dest.writeLong(mFileSize);
        dest.writeInt(mStatus);
    }

}
