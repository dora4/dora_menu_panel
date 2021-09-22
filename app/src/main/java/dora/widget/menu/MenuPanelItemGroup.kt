package dora.widget.menu

class MenuPanelItemGroup(override var title: String? = null,
                         override var titleSpan: MenuPanelItemRoot.Span?
                         = MenuPanelItemRoot.Span(10, 10),
                         override var marginTop: Int = 0,
                         var items: MutableList<MenuPanelItem>)
    : MenuPanelItemRoot {
}