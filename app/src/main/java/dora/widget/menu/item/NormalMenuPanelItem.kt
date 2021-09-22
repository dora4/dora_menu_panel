package dora.widget.menu.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dora.widget.R
import dora.widget.menu.MenuPanelItem
import dora.widget.menu.MenuPanelItemRoot
import java.util.*

class NormalMenuPanelItem @JvmOverloads constructor(override var marginTop: Int = 0,
                                                    override var title: String? = null,
                                                    override var titleSpan: MenuPanelItemRoot.Span? = MenuPanelItemRoot.Span(10, 10),
                                                    override val name: String = UUID.randomUUID().toString().substring(0, 8),
                                                    private val isShowArrowIcon: Boolean = true,
                                                    private val arrowText: String? = null) : MenuPanelItem {

    override fun inflateView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.menu_panel_normal, null)
    }

    override fun initData(menuView: View) {
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = marginTop
        menuView.layoutParams = lp
        val menuTextView = menuView.findViewById<TextView>(R.id.tv_menu_panel_normal_menu)
        val arrowIconView = menuView.findViewById<ImageView>(R.id.iv_menu_panel_normal_arrow)
        val arrowTextView = menuView.findViewById<TextView>(R.id.tv_menu_panel_normal_arrow)
        menuTextView.text = name
        if (isShowArrowIcon) {
            arrowIconView.visibility = View.VISIBLE
        } else {
            arrowIconView.visibility = View.INVISIBLE
        }
        arrowTextView.text = arrowText
    }
}