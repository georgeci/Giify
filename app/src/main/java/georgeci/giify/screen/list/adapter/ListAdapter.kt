package georgeci.giify.screen.list.adapter

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.rxrelay2.PublishRelay
import georgeci.giify.R
import georgeci.giify.extra.getFirstVisibleItemPosition
import georgeci.giify.extra.getLastVisibleItemPosition
import georgeci.giify.model.ImageEntity
import jp.wasabeef.recyclerview.internal.ViewHelper
import kotlin.math.roundToInt


class ListAdapter(
        private val context: Context,
        private val glide: Glide
) : RecyclerView.Adapter<ListViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var reccyler: RecyclerView? = null
    private val items = mutableListOf<ItemWrapper>()


    override fun getItemViewType(position: Int): Int {
        return items[position].viewType.id
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.CONTENT -> ListItemViewHolder(inflater.inflate(R.layout.image_item, parent, false))
            ViewType.ERROR -> ListErrorViewHolder(inflater.inflate(R.layout.error_item, parent, false)).apply {
                loadMore.setOnClickListener { laodMoreClick.accept(Unit) }
            }
            ViewType.PROGRESS -> ListProgressViewHolder(inflater.inflate(R.layout.progress_item, parent, false))
        }
    }

    override fun onViewRecycled(holder: ListViewHolder) {
        map.remove(holder)
    }

    private val map = mutableListOf<ListItemViewHolder>()

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        when (holder) {
            is ListItemViewHolder -> {
                holder.text.visibility = View.VISIBLE
                val item = (items[position] as ItemWrapper.Content).item
                ViewCompat.setTransitionName(holder.image, "Trans:$position")
                val requestOptions = RequestOptions()

                //fix shuffle issue with StaggeredGridLayoutManager
                val width = holder.image.width
                val ratio = item.height.toFloat() / item.width.toFloat()
                holder.image.setAspectRatioEnabled(true)
                holder.image.setAspectRatio(ratio)

                if (width > 0) {
                    val height = (ratio * width).roundToInt()
                    requestOptions.override(width, height)
                }

                Glide.with(holder.image)
                        .asDrawable()
                        .load(item.path)
                        .listener(object : RequestListener<Drawable> {
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                holder.text.visibility = View.GONE
                                return false
                            }

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                holder.text.visibility = View.GONE
                                return false
                            }


                        })
                        .apply(requestOptions)
                        .into(holder.image)
                holder.itemView.setOnClickListener {
                    itemClick.accept(position to item)
                }
                holder.position = position
                map.add(holder)

                reccyler?.apply {
                    when {
                        position > this.getLastVisibleItemPosition() -> for (anim in getBelowAnimators(holder.itemView)) {
                            anim.setDuration(mDuration.toLong()).start()
                            anim.interpolator = mInterpolator
                        }
                        position < this.getFirstVisibleItemPosition() -> for (anim in getAboveAnimators(holder.itemView)) {
                            anim.setDuration(mDuration.toLong()).start()
                            anim.interpolator = mInterpolator
                        }
                        else -> ViewHelper.clear(holder.itemView)
                    }
                }
            }
            is ListErrorViewHolder -> {

            }
            is ListProgressViewHolder -> {
            }
        }
    }

    private var mLastPosition = -1

    private var isFirstOnly = true

    private var mDuration = 300
    private var mInterpolator: Interpolator = LinearInterpolator()

    fun getBelowAnimators(view: View): Array<Animator> {
        return arrayOf(
                ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0.0F)
        )
    }

    fun getAboveAnimators(view: View): Array<Animator> {
        return arrayOf(
                ObjectAnimator.ofFloat(view, "translationY", -view.measuredHeight.toFloat(), 0.0F)
        )
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        map.clear()
        reccyler = null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        reccyler = recyclerView
    }

    val itemClick = PublishRelay.create<Pair<Int, ImageEntity>>()
    val laodMoreClick = PublishRelay.create<Unit>()

    sealed class ItemWrapper(
            val viewType: ViewType
    ) {
        class Content(val item: ImageEntity) : ItemWrapper(ViewType.CONTENT)
        object Error : ItemWrapper(ViewType.ERROR)
        object Progress : ItemWrapper(ViewType.PROGRESS)
    }

    private enum class ViewType(val id: Int) {
        CONTENT(0),
        ERROR(1),
        PROGRESS(2),
    }

    fun update(listOfWrappers: List<ItemWrapper>) {
        Log.i("QOQ", "new size: ${listOfWrappers.size}")
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(items, listOfWrappers))
        items.clear()
        items.addAll(listOfWrappers)

        //fix reset to first position
        val recyclerViewState = reccyler?.layoutManager?.onSaveInstanceState()
        diffResult.dispatchUpdatesTo(this)
        reccyler?.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    fun getImageForTransition(position: Int): ImageView? {
        return map.firstOrNull { it.position == position }?.image
    }
}