package dora.widget.menu

import android.text.TextUtils

interface MenuPanelItemRoot {

    // 菜单的标题。
    var title: String?

    var titleSpan: Span?

    // 菜单的上边距。
    var marginTop: Int

    fun hasTitle(): Boolean = (title != null && !TextUtils.isEmpty(title) && titleSpan != null)

    class Span {
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        constructor()

        // 根据水平间距和垂直间距设置四周的间距，常用。
        constructor(horizontal: Int, vertical: Int) : this(horizontal, vertical, horizontal, vertical)

        constructor(left: Int, top: Int, right: Int, bottom: Int) {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }
    }
}