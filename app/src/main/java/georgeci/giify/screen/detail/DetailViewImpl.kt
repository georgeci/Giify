package georgeci.giify.screen.detail

import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jakewharton.rxbinding2.view.clicks
import georgeci.giify.R
import georgeci.giify.extra.StateWidget

class DetailViewImpl(
        private val view: View,
        private val activity: AppCompatActivity,
        private val transitionName: String?
) : DetailView {
    private val stateWidget: StateWidget = view.findViewById(R.id.detail_state)
    private val image: ImageView = view.findViewById(R.id.detail_image)
    private val profile: View = view.findViewById(R.id.detail_profile)
    private val toolbar: Toolbar = view.findViewById(R.id.detail_toolbar)
    private val userAvatar: ImageView = view.findViewById(R.id.detail_user_avatar)
    private val name: TextView = view.findViewById(R.id.detail_user_name)
    private val realName: TextView = view.findViewById(R.id.detail_user_real_name)
    private val twitter: TextView = view.findViewById(R.id.detail_user_twitter)
    private val retry: Button = view.findViewById(R.id.detail_error_state_button)

    init {
        toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }

    override val userClickIntent = profile.clicks()
    override val retryIntent = retry.clicks()

    override fun consumeState(state: DetailState) {
        when (state) {
            DetailState.Waiting -> {
                stateWidget.show = R.id.detail_content_state
            }
            is DetailState.Loading -> {
                stateWidget.show = R.id.detail_progress_state
            }
            is DetailState.Error -> {
                stateWidget.show = R.id.detail_error_state
            }
            is DetailState.Content -> {
                stateWidget.show = R.id.detail_content_state
                ViewCompat.setTransitionName(image, transitionName)
                if (state.item.user == null) {
                    profile.visibility = View.GONE
                } else {
                    profile.visibility = View.VISIBLE
                    realName.text = view.context.getString(R.string.realname_pattern, state.item.user.displayName)
                    name.text = view.context.getString(R.string.name_pattern, state.item.user.displayName)
                    twitter.visibility = if (state.item.user.twitter != null) View.VISIBLE else View.GONE
                    state.item.user.twitter?.apply {
                        twitter.text = view.context.getString(R.string.twitter_pattern, this)
                    }
                    Glide.with(activity)
                            .load(state.item.user.avatarPath)
                            .into(userAvatar)
                }
                Glide.with(activity)
                        .load(state.item.path)
                        .listener(object : RequestListener<Drawable> {
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                activity.supportStartPostponedEnterTransition()
                                return false
                            }

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                activity.supportStartPostponedEnterTransition()
                                return false
                            }
                        })
                        .into(image)
            }
        }
    }
}