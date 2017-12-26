package georgeci.giify.screen.list

import android.app.Activity
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import georgeci.giify.model.ImageEntity
import georgeci.giify.screen.detail.DetailActivity
import georgeci.giify.screen.list.adapter.ListAdapter


interface ListRouter {
    fun route(position: Int, item: ImageEntity)
}

class ListRouterImpl(
        private val activity: Activity,
        private val adapter: ListAdapter
) : ListRouter {

    override fun route(position: Int, item: ImageEntity) {
        val imageForTransition = adapter.getImageForTransition(position)
        val intent = DetailActivity.intent(activity, item)
        if (imageForTransition != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val transitionName = ViewCompat.getTransitionName(imageForTransition)
            intent.putExtra(DetailActivity.TRANSITION_NAME, transitionName)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    imageForTransition,
                    transitionName)

            activity.startActivity(intent, options.toBundle())
        } else {
            activity.startActivity(intent)
        }
    }
}