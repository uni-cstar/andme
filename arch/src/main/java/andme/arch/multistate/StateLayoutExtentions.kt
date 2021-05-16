package andme.arch.multistate

import andme.arch.R
import andme.core.binding.bindTextOrGone
import andme.core.statelayout.StateView
import andme.core.widget.setGone
import android.view.View
import android.widget.Button
import android.widget.TextView

/**
 * Created by Lucio on 2021/5/16.
 */

fun StateView.showLoadingMsg(msg: CharSequence?) {
    showTextWithoutButton(msg)
}

fun StateView.showEmptyMsgWithButton(
    msg: CharSequence,
    buttonText: String = "重试",
    requestFocus: Boolean = false,
    onClick: (View) -> Unit
) {
    showTextWithButton(msg, buttonText, requestFocus, onClick)
}

fun StateView.showEmptyMsgWithoutButton(msg: CharSequence) {
    showTextWithoutButton(msg)
}

fun StateView.showErrorMsgWithButton(
    msg: CharSequence,
    buttonText: String = "重试",
    requestFocus: Boolean = false,
    onClick: (View) -> Unit
) {
   showTextWithButton(msg, buttonText, requestFocus, onClick)
}

fun StateView.showErrorMsgWithoutButton(msg: CharSequence) {
    showTextWithoutButton(msg)
}


private fun StateView.showTextWithButton(
    msg: CharSequence?,
    buttonText: String = "重试",
    requestFocus: Boolean = false,
    onClick: (View) -> Unit
) {
    textViewAM?.bindTextOrGone(msg)
    buttonAM?.let {
        it.visibility = View.VISIBLE
        it.text = buttonText
        it.setOnClickListener(onClick)
        it.isFocusable = true
        it.isFocusableInTouchMode = true
        if (requestFocus) {
            it.requestFocus()
        }
    }
}

private fun StateView.showTextWithoutButton(msg: CharSequence?) {
    textViewAM?.bindTextOrGone(msg)
    buttonAM?.visibility = View.GONE
}

fun StateView.setRetryButton(
    text: String = "重试",
    requestFocus: Boolean = false,
    onClick: (View) -> Unit
) {
    buttonAM?.let {
        it.setText(text)
        it.setOnClickListener(onClick)
        it.isFocusable = true
        it.isFocusableInTouchMode = true
        if (requestFocus) {
            it.requestFocus()
        }
    }
}


private val StateView.imageViewAM: Button? get() = view.findViewById(R.id.am_id_state_view_image)


private val StateView.textViewAM: TextView? get() = view.findViewById(R.id.am_id_state_view_text)


private val StateView.buttonAM: Button? get() = view.findViewById(R.id.am_id_state_view_button)