package dora.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import dora.widget.menu.MenuPanelItem
import dora.widget.menu.MenuPanelItemGroup
import dora.widget.menu.MenuPanelItemRoot
import java.util.*

/**
 * 通用功能菜单，类似于RecyclerView。
 */
open class MenuPanel : ScrollView, View.OnClickListener {

    // 面板的背景颜色，一般为浅灰色。
    private var panelBgColor = -0xa0a07
    private var items: MutableList<MenuPanelItem> = ArrayList()
    private var itemViewsCache: MutableList<View> = ArrayList()
    private var onPanelMenuClickListener: OnPanelMenuClickListener? = null
    private var onPanelScrollListener: OnPanelScrollListener? = null
    private val menuGroupInfo: MutableList<GroupInfo> = ArrayList()
    private val listenerInfo = LinkedList<ListenerDelegate>()
    private lateinit var panelRoot: FrameLayout

    // 存放Menu和Custom View。
    protected lateinit var container: LinearLayout

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun removeItem(item: MenuPanelItem): MenuPanel {
        val position = seekForItemPosition(item)
        if (position != SEEK_FOR_ITEM_ERROR_NOT_FOUND &&
                position != SEEK_FOR_ITEM_ERROR_MISS_MENU_NAME) {
            removeItem(position)
        } else {
            Log.e(TAG, "failed to seekForItemPosition，$position")
        }
        return this
    }

    private fun init(context: Context) {
        isFillViewport = true
        addContainer(context)
    }

    fun setOnPanelMenuClickListener(l: OnPanelMenuClickListener?) {
        onPanelMenuClickListener = l
    }

    fun setOnPanelScrollListener(l: OnPanelScrollListener?) {
        onPanelScrollListener = l
    }

    @JvmOverloads
    fun parseItemView(item: MenuPanelItem, isLoadData: Boolean = false): View {
        val menuView = item.inflateView(context)
        if (isLoadData) {
            item.initData(menuView)
        }
        return menuView
    }

    fun getItem(position: Int): MenuPanelItem? {
        return items[position]
    }

    fun getGroupInfo(item: MenuPanelItem?): GroupInfo? {
        for (groupInfo in menuGroupInfo) {
            if (groupInfo.hasItem(item)) {
                return groupInfo
            }
        }
        return null
    }

    /**
     * 根据item的position移除一个item，此方法被多处引用，修改前需要理清布局层级结构。
     *
     * @param position
     * @return
     */
    fun removeItem(position: Int): MenuPanel {
        val item = items[position]
        val groupInfo = getGroupInfo(item)
        val belongToGroup = groupInfo != null
        val view = getCacheViewFromPosition(position)
        if (!belongToGroup) {
            container.removeView(view)
        } else {
            //属于一个组
            val menuGroupCard = groupInfo!!.groupMenuCard
            menuGroupCard.removeView(view)
            groupInfo.removeItem(item)
            //一个组内的item全部被移除后，也移除掉这个组
            if (groupInfo.isEmpty) {
                //连同title一起移除
                container.removeView(menuGroupCard)
                menuGroupInfo.remove(groupInfo)
            }
        }
        items.removeAt(position)
        itemViewsCache.removeAt(position)
        listenerInfo.removeAt(position)
        return this
    }

    /**
     * 清空所有item和相关view。
     */
    fun clearAll(): MenuPanel {
        if (items.size > 0) {
            items.clear()
        }
        container.removeAllViews()
        itemViewsCache.clear()
        menuGroupInfo.clear()
        listenerInfo.clear()
        return this
    }

    /**
     * 移除连续的item。
     *
     * @param start 第一个item的下标，包括
     * @param end   最后一个item的下标，包括
     * @return
     */
    fun removeItemRange(start: Int, end: Int): MenuPanel {
        for (i in start until end + 1) {
            removeItem(start)
        }
        return this
    }

    /**
     * 从某个位置移除到最后一个item。
     *
     * @param start 第一个item的下标，包括
     * @return
     */
    fun removeItemFrom(start: Int): MenuPanel {
        val end = items.size - 1
        if (start <= end) {
            //有就移除
            removeItemRange(start, end)
        }
        return this
    }

    /**
     * 从第一个item移除到某个位置。
     *
     * @param end 最后一个item的下标，包括
     * @return
     */
    fun removeItemTo(end: Int): MenuPanel {
        val start = 0
        removeItemRange(start, end)
        return this
    }

    val itemCount: Int
        get() = items.size

    fun addMenuGroup(itemGroup: MenuPanelItemGroup): MenuPanel {
        val hasTitle = itemGroup.hasTitle()
        val items = itemGroup.items
        val menuGroupCard = LinearLayout(context)
        menuGroupCard.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = itemGroup.marginTop
        menuGroupCard.layoutParams = lp
        if (hasTitle) {
            val titleView = TextView(context)
            titleView.setPadding(itemGroup.titleSpan!!.left, itemGroup.titleSpan!!.top,
                    itemGroup.titleSpan!!.right, itemGroup.titleSpan!!.bottom)
            titleView.text = itemGroup.title
            titleView.textSize = 15f
            titleView.setTextColor(-0x666667)
            menuGroupCard.addView(titleView)
        }
        for (item in items) {
            // 清除组内item的边距等
            applyDefault(item)
            addMenuToCard(item, menuGroupCard)
        }
        container.addView(menuGroupCard)
        // 保存菜单组信息
        menuGroupInfo.add(GroupInfo(hasTitle, items, menuGroupCard))
        return this
    }

    override fun addView(child: View) {
        if (child !is FrameLayout) {
            return
        }
        if (childCount > 1) {
            return
        }
        super.addView(child)
    }

    private fun addContainer(context: Context) {
        panelRoot = FrameLayout(context)
        container = LinearLayout(context)
        container.orientation = LinearLayout.VERTICAL
        container.setBackgroundColor(panelBgColor)
        panelRoot.addView(container)
        addView(panelRoot)
    }

    fun addMenu(item: MenuPanelItem): MenuPanel {
        val menuView = bindItemListener(item)
        if (!item.hasTitle()) {
            container.addView(menuView)
        } else {
            val titleView = TextView(context)
            titleView.setPadding(item.titleSpan!!.left, item.titleSpan!!.top,
                    item.titleSpan!!.right, item.titleSpan!!.bottom)
            titleView.text = item.title
            titleView.textSize = 15f
            titleView.setTextColor(-0x666667)
            val menuCard = LinearLayout(context)
            menuCard.orientation = LinearLayout.VERTICAL
            menuCard.addView(titleView)
            menuCard.addView(menuView)
            container.addView(menuCard)
        }
        return this
    }

    private fun addMenuToCard(item: MenuPanelItem, container: LinearLayout) {
        val menuView = bindItemListener(item)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = item.marginTop
        menuView.layoutParams = lp
        container.addView(menuView)
    }

    fun seekForItemPosition(item: MenuPanelItem): Int {
        for (i in items.indices) {
            val mpi = items[i]
            val menuName = mpi.name
            if (menuName == "" || item.name == "") {
                return SEEK_FOR_ITEM_ERROR_MISS_MENU_NAME //失去菜单名称
            }
            if (menuName == item.name) {
                return i
            }
        }
        return SEEK_FOR_ITEM_ERROR_NOT_FOUND
    }

    /**
     * 获取MenuPanel中条目布局中的子控件，推荐使用。
     *
     * @param position
     * @param viewId
     * @return
     */
    fun getCacheChildView(position: Int, viewId: Int): View? {
        val menuView = getCacheViewFromPosition(position)
        return menuView?.findViewById(viewId)
    }

    /**
     * 获取item的view，用于修改item的数据。
     *
     * @param item
     * @return
     */
    fun getCacheViewFromItem(item: MenuPanelItem): View? {
        val position = seekForItemPosition(item)
        return if (position != SEEK_FOR_ITEM_ERROR_NOT_FOUND &&
                position != SEEK_FOR_ITEM_ERROR_MISS_MENU_NAME) {
            getCacheViewFromPosition(position)
        } else null
    }

    /**
     * 获取item的view，用于修改item的数据。
     *
     * @param position item的位置，从0开始
     * @return
     */
    fun getCacheViewFromPosition(position: Int): View? {
        return if (position < itemViewsCache.size) {
            itemViewsCache[position]
        } else null
    }

    protected fun getCacheViewFromTag(tag: String): View? {
        for (delegate in listenerInfo) {
            if (delegate.tag == tag) {
                val position = delegate.position
                return getCacheViewFromPosition(position)
            }
        }
        return null
    }

    /**
     * 绑定item的点击事件。
     *
     * @param item
     * @return 绑定成功后返回item的view
     */
    private fun bindItemListener(item: MenuPanelItem): View {
        items.add(item)
        //解析Item所对应的布局，并调用item的initData
        val menuView = parseItemView(item, true)
        itemViewsCache.add(menuView)
        val tag = UUID.randomUUID().toString().substring(0, 16)
        menuView.tag = tag
        val delegate = getListenerInfo(tag)
        menuView.setOnClickListener(delegate)
        listenerInfo.add(delegate)
        return menuView
    }

    private fun applyDefault(item: MenuPanelItem) {
        item.marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f,
                resources.displayMetrics).toInt() // item的上边距修改为1dp
        item.title = "" // item去掉标题
        item.titleSpan = MenuPanelItemRoot.Span() // item去掉标题边距
    }

    // 不是IMenu，所以不会影响菜单的点击事件位置，但需要自己处理控件内部的点击事件。
    fun <T : View> addCustomView(view: T): MenuPanel {
        container.addView(view)
        return this
    }

    fun <T : View> addCustomView(view: T, index: Int): MenuPanel {
        container.addView(view, index)
        return this
    }

    fun removeCustomViewAt(position: Int): MenuPanel {
        if (container.childCount > position) {
            // 有就移除
            container.removeViewAt(position)
        }
        return this
    }

    /**
     * 样式等参数改变才需要更新，只有类似于addItem、removeItem这样的，不需要调用此方法。
     */
    open fun updatePanel() {
        requestLayout()
    }

    private fun getListenerInfo(tag: String): ListenerDelegate {
        return ListenerDelegate(tag, items.size - 1, this)
    }

    class GroupInfo(var hasTitle: Boolean, var items: MutableList<MenuPanelItem>, var groupMenuCard: LinearLayout) {
        fun hasItem(item: MenuPanelItem?): Boolean {
            return items.contains(item)
        }

        val itemCount: Int
            get() = items.size

        fun addItem(item: MenuPanelItem) {
            items.add(item)
        }

        fun removeItem(item: MenuPanelItem) {
            items.remove(item)
        }

        val isEmpty: Boolean
            get() = items.size == 0
    }

    override fun onClick(v: View) {
        val tag = v.tag as String
        for (delegate in listenerInfo) {
            if (delegate.tag == tag) {
                val clickPos = delegate.position
                onPanelMenuClickListener?.onMenuClick(clickPos, v, items[clickPos].name)
                break
            }
        }
    }

    fun setPanelBgColor(color: Int): MenuPanel {
        panelBgColor = color
        container.setBackgroundColor(panelBgColor)
        return this
    }

    interface OnPanelMenuClickListener {

        fun onMenuClick(position: Int, view: View, name: String)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (scrollY == 0) {
            onPanelScrollListener?.onScrollToTop()
        } else if (panelRoot.measuredHeight == scrollY + height) {
            onPanelScrollListener?.onScrollToBottom()
        }
    }

    interface OnPanelScrollListener {
        fun onScrollToTop()
        fun onScrollToBottom()
    }

    class ListenerDelegate(val tag: String, val position: Int, private val mListener: OnClickListener) : OnClickListener {
        override fun onClick(v: View) {
            mListener.onClick(v)
        }
    }

    companion object {
        private const val TAG = "MenuPanel"
        private const val SEEK_FOR_ITEM_ERROR_NOT_FOUND = -1
        private const val SEEK_FOR_ITEM_ERROR_MISS_MENU_NAME = -2
    }
}