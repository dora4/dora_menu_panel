package dora.widget.menu

import android.content.Context
import android.view.View

/**
 * 实现它来自定义面板的菜单，你也可以使用[dora.widget.menu.AbsMenuPanelItem]。
 */
interface MenuPanelItem : MenuPanelItemRoot {

    fun inflateView(context: Context): View
    val name: String
    fun initData(menuView: View)
}