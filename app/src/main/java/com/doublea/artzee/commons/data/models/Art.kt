package com.doublea.artzee.commons.data.models

import android.os.Parcel
import android.os.Parcelable

data class Art(
        val id: String,
        val title: String,
        val category: String,
        val medium: String,
        val date: String,
        val collectingInstitution: String,
        val image_versions: List<String>,
        val thumbnail: String,
        val image: String,
        val partner: String,
        val genes: String,
        val artists: String,
        val similarArtworks: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeString(medium)
        parcel.writeString(date)
        parcel.writeString(collectingInstitution)
        parcel.writeStringList(image_versions)
        parcel.writeString(thumbnail)
        parcel.writeString(image)
        parcel.writeString(partner)
        parcel.writeString(genes)
        parcel.writeString(artists)
        parcel.writeString(similarArtworks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Art> {
        override fun createFromParcel(parcel: Parcel): Art {
            return Art(parcel)
        }

        override fun newArray(size: Int): Array<Art?> {
            return arrayOfNulls(size)
        }
    }
}