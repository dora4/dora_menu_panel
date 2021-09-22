package dora.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 自动给最后加一行提示信息，如共有几条记录的菜单面板。
 */
class TipsMenuPanel : MenuPanel {

    private var tips: String? = ""
    private var tipsView: TextView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setEmptyTips(): TipsMenuPanel {
        setTips("")
        updatePanel()
        return this
    }

    fun setTips(tips: String): TipsMenuPanel {
        this.tips = tips
        updatePanel()
        return this
    }

    override fun updatePanel() {
        if (tipsView != null) {
            container.removeView(tipsView)
        }
        if (tips != null && tips!!.isNotEmpty()) {
            tipsView = TextView(context)
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.topMargin = 10
            lp.bottomMargin = 10
            tipsView!!.gravity = Gravity.CENTER_HORIZONTAL
            tipsView!!.setTextColor(-0x666667)
            tipsView!!.layoutParams = lp
            tipsView!!.text = tips
            //增加了底部的tips
            container.addView(tipsView)
        }
        super.updatePanel()
    }
}