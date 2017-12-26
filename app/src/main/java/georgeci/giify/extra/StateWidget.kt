package georgeci.giify.extra

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import georgeci.giify.R

class StateWidget(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    companion object {
        private const val NO_ID = -1
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context) :
            this(context, null)

    @LayoutRes
    private var _show: Int = NO_ID

    var show: Int
        @LayoutRes
        get() = _show
        set(@LayoutRes value) {
            findAndShow(value)
            _show = value
        }

    init {
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.StateWidget,
                    defStyleAttr,
                    defStyleRes)
            show = a.getResourceId(R.styleable.StateWidget_show, NO_ID)
            a.recycle()
        }
    }

    fun changeShowedViewById(@IdRes id: Int) {
        show = id
    }

    private fun findAndShow(@LayoutRes value: Int) {
        (0 until childCount)
                .forEach {
                    val view = getChildAt(it)
                    when (value) {
                        view.id -> view.visibility = View.VISIBLE
                        else -> view.visibility = View.GONE
                    }
                }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        show = _show
    }

}