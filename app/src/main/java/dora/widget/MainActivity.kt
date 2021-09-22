package dora.widget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import dora.widget.menu.MenuPanelItemGroup
import dora.widget.menu.MenuPanelItemRoot
import dora.widget.menu.item.InputMenuPanelItem
import dora.widget.menu.item.NormalMenuPanelItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val menuPanel = findViewById<TipsMenuPanel>(R.id.menuPanel)
        menuPanel
                .addMenu(NormalMenuPanelItem(name = "普通条目1"))
                .addMenu(NormalMenuPanelItem(name = "普通条目2"))
                .addMenu(NormalMenuPanelItem(name = "普通条目3"))
                .addMenu(NormalMenuPanelItem(name = "普通条目4"))
                .addMenu(InputMenuPanelItem(name = "输入条目", hint = "输入文本"))
                .addMenuGroup(MenuPanelItemGroup(title = "这是第一个分组的标题",
                        marginTop = 10,
                        items = arrayListOf(NormalMenuPanelItem(name = "分组条目1"),
                        NormalMenuPanelItem(name = "分组条目2"))))
                .addMenuGroup(MenuPanelItemGroup(title = "这是第二个分组的标题",
                        titleSpan = MenuPanelItemRoot.Span(20, 20),
                        items = arrayListOf(NormalMenuPanelItem(name = "分组条目3"),
                                NormalMenuPanelItem(name = "分组条目4"))))
                .addMenu(NormalMenuPanelItem(marginTop = 20, name = "添加菜单"))
        menuPanel.setTips("总共有${menuPanel.itemCount}个菜单")
        menuPanel.setOnPanelMenuClickListener(object : MenuPanel.OnPanelMenuClickListener {

            override fun onMenuClick(position: Int, view: View, name: String) {
                if (name == "添加菜单") {   // 强烈推荐使用name判断功能而不是position
                    menuPanel.addMenu(NormalMenuPanelItem())
                    menuPanel.setTips("总共有${menuPanel.itemCount}个菜单")
                } else {
                    Toast.makeText(this@MainActivity, "点击了$name", Toast.LENGTH_SHORT).show()
                }
            }
        })
        menuPanel.setOnPanelScrollListener(object : MenuPanel.OnPanelScrollListener {
            override fun onScrollToTop() {
                Toast.makeText(this@MainActivity, "滑动到顶部", Toast.LENGTH_SHORT).show()
            }

            override fun onScrollToBottom() {
                Toast.makeText(this@MainActivity, "滑动到底部", Toast.LENGTH_SHORT).show()
            }
        })
    }
}