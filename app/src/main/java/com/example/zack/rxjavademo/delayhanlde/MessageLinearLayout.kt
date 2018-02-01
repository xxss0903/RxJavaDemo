package com.example.zack.rxjavademo.delayhanlde

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.LinearLayout
import com.example.zack.rxjavademo.R

class MessageLinearLayout : LinearLayout {
    lateinit var bottomMessage: AppCompatTextView
    private var drawable: Drawable? = null
    private var bottomMessageDrawable: Drawable? = null
    var showBottomMessage: Boolean = false
            // 重写set方法，给底部的信息需要显示时添加一些显示，和背景红色的显示
        set(value) {
            field = value
            if (findViewById<View>(R.id.bottom_message) != null) {
                // 根据value判断是否显示红色提示背景
                if (value) {
                    this.background = bottomMessageDrawable
                    findViewById<View>(R.id.bottom_message).visibility = VISIBLE
                } else {
                    this.background = drawable
                    findViewById<View>(R.id.bottom_message).visibility = GONE
                }
            }
        }

    // (required for XML)
    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    // (required for XML)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    // Providing a style (required for XML)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val layoutId: Int
        val a = context.theme.obtainStyledAttributes(
                attrs!!,
                R.styleable.MessageLinearLayout,
                defStyleAttr, 0)
        try {
            layoutId = a.getResourceId(R.styleable.MessageLinearLayout_bottom_message_layout, R.layout.textview_error)
            View.inflate(context, layoutId, this)

            bottomMessage = findViewById<AppCompatTextView>(R.id.bottom_message)
            bottomMessage.text = a.getString(R.styleable.MessageLinearLayout_bottom_message_text)
            if (a.getDrawable(R.styleable.MessageLinearLayout_bottom_message_icon) != null) {
                bottomMessage.setCompoundDrawablesRelativeWithIntrinsicBounds(a.getDrawable(R.styleable.MessageLinearLayout_bottom_message_icon), null, null, null)
            }
            bottomMessageDrawable = a.getDrawable(R.styleable.MessageLinearLayout_bottom_message_background)

            if (bottomMessageDrawable == null) {
                bottomMessageDrawable = ColorDrawable(context.resources.getColor(R.color.errorRed))
            }

            showBottomMessage = a.getBoolean(R.styleable.MessageLinearLayout_show_bottom_message, false)
        } finally {
            a.recycle()
        }
        drawable = background

    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
        if (background != bottomMessageDrawable) {
            drawable = background
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        var newIndex = index
        if (index < 0 || index == childCount - 1) {
            newIndex = indexOfChild(findViewById(R.id.bottom_message)) - 1
        }
        super.addView(child, newIndex, params)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        reorderView()
        super.onLayout(changed, l, t, r, b)
    }

    private fun reorderView() {
        if (findViewById<View>(R.id.bottom_message) != null && indexOfChild(findViewById(R.id.bottom_message)) != childCount - 1) {
            val view = findViewById<View>(R.id.bottom_message)
            (view.parent as ViewManager).removeView(view)
            addView(view, childCount - 1)
        }
    }
}