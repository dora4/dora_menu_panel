# DoraMenuPanel

描述：菜单面板，简单的列表将不再需要RecyclerView

复杂度：★★☆☆☆

分组：【Dora大控件组】

关系：暂无

技术要点：代码布局View、View滑动处理

### 照片

![avatar](https://github.com/dora4/dora_menu_panel/blob/main/art/dora_menu_panel.jpg)

### 动图

![avatar](https://github.com/dora4/dora_menu_panel/blob/main/art/dora_menu_panel.gif)

### 软件包

https://github.com/dora4/dora_menu_panel/blob/main/art/dora_menu_panel.apk

### 用法

```kotlin
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
```

| 类            | API               | 描述                                     |
| ------------- | ----------------- | ---------------------------------------- |
| MenuPanel     | addMenu()         | 添加一个菜单                             |
| MenuPanel     | addMenuGroup()    | 添加一个菜单组                           |
| MenuPanel     | getItem()         | 获取菜单的基本信息，然后可以进行数据修改 |
| MenuPanel     | removeItem()      | 移除一个菜单                             |
| MenuPanel     | removeItemRange() | 移除连续的菜单                           |
| MenuPanel     | removeItemFrom    | 从某个位置移除该位置后面的菜单           |
| MenuPanel     | removeItemTo      | 从开始移除到某个位置的菜单               |
| TipsMenuPanel | setTips()         | 设置底部提示信息                         |

