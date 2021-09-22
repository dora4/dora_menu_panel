package dora.widget.menu.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import dora.widget.R
import dora.widget.menu.IMenu
import dora.widget.menu.MenuPanelItem
import dora.widget.menu.MenuPanelItemRoot
import java.util.*

/**
 * 可用它自定义面板菜单。
 *
 * @param <T> 数据实体类
 */
abstract class AbsMenuPanelItem<T : IMenu> @JvmOverloads constructor(menu: T, override var marginTop: Int = 0, override var title: String? = "",
                                           override var titleSpan: MenuPanelItemRoot.Span? =
                                                   MenuPanelItemRoot.Span(10, 10),
                                           override var name: String = UUID.randomUUID().toString().substring(0, 8)) : MenuPanelItem {

    var layoutId: Int = 0

    override fun inflateView(context: Context): View {
        val view = LayoutInflater.from(context).inflate(layoutId, null)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = marginTop
        view.layoutParams = lp
        return view
    }
}