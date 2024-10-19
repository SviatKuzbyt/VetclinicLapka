package ua.sviatkuzbyt.vetcliniclapka.ui.elements
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.R
class TwoButtonsToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var backButton: Button
    private var actionButton: Button
    private var label: TextView
    init {
        LayoutInflater.from(context).inflate(R.layout.toolbar_two_buttons, this, true)
        backButton = findViewById(R.id.btnToolbarBack)
        actionButton = findViewById(R.id.btnOtherAction)
        label = findViewById(R.id.labelToolbarBack)
    }
    fun setup(text: String, activity: AppCompatActivity, actionIcon: Int, action: () -> Unit) {
        label.text = text
        backButton.setOnClickListener {
            activity.finish()
        }
        actionButton.setBackgroundResource(actionIcon)
        actionButton.setOnClickListener{
            action.invoke()
        }
    }
}