package ua.sviatkuzbyt.vetcliniclapka.ui.elements.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.R

class CenterToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var button: Button
    private var label: TextView
    private var confirmCancelWindow: ConfirmCancelWindow? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.my_view_toolbar, this, true)
        button = findViewById(R.id.btnToolbarBack)
        label = findViewById(R.id.labelToolbarBack)
    }

    fun setup(text: String, activity: AppCompatActivity) {
        label.text = text
        button.setOnClickListener {
            activity.finish()
        }
    }

    fun setupWithConfirmWindow(text: String, activity: AppCompatActivity){
        confirmCancelWindow = ConfirmCancelWindow(activity)
        label.text = text
        button.setOnClickListener {
            confirmCancelWindow?.showWindow()
        }

        activity.onBackPressedDispatcher.addCallback { confirmCancelWindow?.showWindow() }
    }
}