package com.zb.mrseo.utility;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class AddProductImage implements Parcelable {

    public String productImageId;
    public String productImage;
    public String imageURI;
    public Uri uri;

    public AddProductImage()
    {}


    protected AddProductImage(Parcel in) {
        productImageId = in.readString();
        productImage = in.readString();
        imageURI = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productImageId);
        dest.writeString(productImage);
        dest.writeString(imageURI);
        dest.writeParcelable(uri, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddProductImage> CREATOR = new Creator<AddProductImage>() {
        @Override
        public AddProductImage createFromParcel(Parcel in) {
            return new AddProductImage(in);
        }

        @Override
        public AddProductImage[] newArray(int size) {
            return new AddProductImage[size];
        }
    };
}
