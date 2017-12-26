package georgeci.giify.model

import android.os.Parcel
import android.os.Parcelable

class User(
        val avatarPath: String?,
        val profile_url: String,
        val username: String,
        val displayName: String,
        val twitter: String?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(avatarPath)
        writeString(profile_url)
        writeString(username)
        writeString(displayName)
        writeString(twitter)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}