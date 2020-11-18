package nz.snapitgroup.vmssc

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val allowedChar = "0123456789abcdefABCDEF".toCharArray()
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?;

        editText.requestFocus()
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                if(inputIsNotValid())
                    textView.setText("This does not seem like Bluetooth address")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Text Count", count.toString())
                if (editText.text.length == 6 && !inputIsNotValid()){
                    val loginDetail = LoginDetail(editText.text.toString())
                    textView.setText(loginDetail.getSc())
                    copyBtn.visibility = View.VISIBLE
                } else if (editText.text.length > 6){
                    textView.setText("Length is over 6")
                    copyBtn.visibility = View.INVISIBLE
                } else{
                    textView.setText("")
                    copyBtn.visibility = View.INVISIBLE
                }
            }

        })
    }

    private fun inputIsNotValid(): Boolean {
        if (editText.text.isEmpty()) return false
        val charArr = editText.text.toString().toCharArray()
        for (c in charArr) {
            if (!(c in allowedChar)){
                return true
            }
        }
        return false
    }

    // on click copy button
    fun copyText(view: View) {
        myClip = ClipData.newPlainText("text", textView.text)
        myClipboard?.setPrimaryClip(myClip!!)

        val toast = Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,200)
        toast.show()
    }

}
