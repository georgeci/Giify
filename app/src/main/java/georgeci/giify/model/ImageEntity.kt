package georgeci.giify.model

import android.os.Parcel
import android.os.Parcelable

data class ImageEntity(
        val id: String,
        val path: String,
        val width: Int,
        val height: Int,
        val user: User?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readParcelable<User>(User::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(path)
        writeInt(width)
        writeInt(height)
        writeParcelable(user, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImageEntity> = object : Parcelable.Creator<ImageEntity> {
            override fun createFromParcel(source: Parcel): ImageEntity = ImageEntity(source)
            override fun newArray(size: Int): Array<ImageEntity?> = arrayOfNulls(size)
        }
    }
}