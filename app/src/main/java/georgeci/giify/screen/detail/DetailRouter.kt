package georgeci.giify.screen.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import georgeci.giify.model.ImageEntity
import io.reactivex.Observable
import kategory.Either
import android.support.customtabs.CustomTabsIntent



interface DetailRouter {
    val loadByIntent: Observable<Either<String, ImageEntity>>
    fun openUser(path: String)
}

class DetailRouterImpl(
        private val activity: Activity,
        private val intentStream: Observable<Intent>
) : DetailRouter {

    override val loadByIntent = intentStream
            .map {
                if (it.action == Intent.ACTION_VIEW) {
                    val id = it.data.lastPathSegment
                    Either.Left<String, ImageEntity>(id, Unit)
                } else {
                    val entity = it.extras.getParcelable<ImageEntity>(DetailActivity.IMAGE_KEY)
                    Either.Right<String, ImageEntity>(entity, Unit)
                }
            }

    override fun openUser(path: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(activity, Uri.parse(path))
    }
}