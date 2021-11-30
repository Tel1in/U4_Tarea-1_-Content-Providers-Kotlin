package mx.tecnm.tepic.t1u4

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main2.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity2 : AppCompatActivity() {
    val sipermiso2 = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)!=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS),sipermiso2)
        }

        button3.setOnClickListener {
            consultarMensajes()
        }

        button4.setOnClickListener {
            finish()
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==sipermiso2){
            consultarMensajes()
        }
    }

    @SuppressLint("Range")
    private fun consultarMensajes() {
        var resulto = ""
        textView2.setText("")
        var cursorBorradores = contentResolver.query(
            Uri.parse("content://sms/draft"),
            null,null,null,null
        )
        if(cursorBorradores!!.moveToFirst()){
            do{
                var fechal = ""
                var numero = cursorBorradores.getString(cursorBorradores.getColumnIndex("address"))
                var fecha = cursorBorradores.getLong(cursorBorradores.getColumnIndex("date"))
                fechal=convertLongtoTime(fecha)
                var contenido = cursorBorradores.getString(cursorBorradores.getColumnIndex("body"))
                resulto= "Numero: ${numero}\nFecha: ${fechal}\nContenido: ${contenido}\n------------\n"
            }while (cursorBorradores.moveToNext())
            textView2.setText(resulto)
        }else{
            textView2.setText("No hay nada que ver")
        }
    }

    fun convertLongtoTime(time:Long):String{
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }
}

