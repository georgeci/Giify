package georgeci.giify.model.source.response

import com.google.gson.annotations.SerializedName

class User(
        @SerializedName("avatar_url")
        val avatarPath: String?,
        @SerializedName("profile_url")
        val profile_url: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("display_name")
        val displayName: String,
        @SerializedName("twitter")
        val twitter: String?
)