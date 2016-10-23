package com.twistedequations.rxsavestate

import android.view.View
import android.widget.LinearLayout
import com.twistedequations.rxsavestate.sample.R
import org.jetbrains.anko.*


class RxStateActivityUI : AnkoComponent<View> {
    override fun createView(ui: AnkoContext<View>) = with(ui) {
        verticalLayout {

            button("Save State") {
                id = R.id.rx_state_button
                textSize = sp(20f).toFloat()
                jsonTheme("json.theme.key")
            }.lparams { wrapLayoutParams(ui) }

            editText {
                id = R.id.rx_state_edittext
                jsonTheme("json.theme.key")
            }.lparams { wrapLayoutParams(ui) }

        }
    }
}

private val wrapLayoutParams: LinearLayout.LayoutParams.(AnkoContext<*>) -> Unit = { ui ->
    this.width = matchParent
    this.height = wrapContent
    this.margin = ui.dimen(R.dimen.activity_vertical_margin)
}

private val jsonTheme: View.(String) -> Unit = { key ->
    //Load the json theme
    backgroundDrawable = context.resources.getDrawable(R.color.colorAccent, context.theme)
}

private val context: View.(String) -> Unit = { key ->
    //Load the json theme
    backgroundDrawable = context.resources.getDrawable(R.color.colorAccent, context.theme)
}