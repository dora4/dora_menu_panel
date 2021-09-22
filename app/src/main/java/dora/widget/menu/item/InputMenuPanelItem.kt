package dora.widget.menu.item

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import dora.widget.R
import dora.widget.menu.MenuPanelItem
import dora.widget.menu.MenuPanelItemRoot
import java.util.*

class InputMenuPanelItem @JvmOverloads constructor(override var marginTop: Int = 0, override var title: String? = null,
                                                   override var titleSpan: MenuPanelItemRoot.Span? = MenuPanelItemRoot.Span(10, 10),
                                                   override var name: String = UUID.randomUUID().toString().substring(0, 8),
                                                   private val content: String = "",
                                                   private val hint: String = "",
                                                   private val watcher: ContentWatcher? = null) : MenuPanelItem {

    override fun initData(menuView: View) {
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = marginTop
        menuView.layoutParams = lp
        val editText = menuView.findViewById<EditText>(R.id.et_menu_panel_input)
        editText.setText(content)
        editText.hint = hint
        if (!TextUtils.isEmpty(content)) {
            editText.setText(content)
            editText.setSelection(content.length)
        }
        if (watcher != null) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    watcher.onContentChanged(this@InputMenuPanelItem, s.toString())
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    override fun inflateView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.menu_panel_input, null)
    }

    interface ContentWatcher {
        fun onContentChanged(item: InputMenuPanelItem, content: String)
    }
}