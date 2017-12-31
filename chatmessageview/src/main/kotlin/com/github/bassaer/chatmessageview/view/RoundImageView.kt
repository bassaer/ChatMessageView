package com.github.bassaer.chatmessageview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

import com.github.bassaer.chatmessageview.R

/**
 * Round Image view for picture on message
 * Created by nakayama on 2017/03/08.
 */
class RoundImageView : ImageView {
    private var mClipPath: Path? = null

    constructor(context: Context) : super(context) {
        initClipPath()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initClipPath()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initClipPath()
    }

    private fun initClipPath() {

        // Below Jelly Bean, clipPath on canvas would not work because lack of hardware acceleration
        // support. Hence, we should explicitly say to use software acceleration.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        mClipPath = Path()
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val radius = resources.getDimensionPixelSize(R.dimen.view_radius_normal).toFloat()
        mClipPath!!.addRoundRect(rect, radius, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        initClipPath()
        canvas.clipPath(mClipPath!!)
        super.onDraw(canvas)
    }
}
