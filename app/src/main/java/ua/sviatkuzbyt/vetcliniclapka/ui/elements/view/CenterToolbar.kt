package ua.sviatkuzbyt.vetcliniclapka.ui.elements.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.R


class CenterToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var button: Button
    private var label: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.my_view_toolbar, this, true)
        button = findViewById(R.id.btnToolbarBack)
        label = findViewById(R.id.labelToolbarBack)
    }

    fun setup(text: Int, activity: AppCompatActivity) {
        label.setText(text)
        button.setOnClickListener {
            activity.finish()
        }
    }
}