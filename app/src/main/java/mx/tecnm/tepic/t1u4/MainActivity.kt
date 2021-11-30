package mx.tecnm.tepic.t1u4
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val sipermiso = 1
    val sipermiso3 = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG), sipermiso)
        }//FIN IF PERMISO


        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALL_LOG)!=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_CALL_LOG),sipermiso3)
        }


        button.setOnClickListener {
            consultarCPLlamadas()
        }

        button2.setOnClickListener {
            val ff = Intent(this,MainActivity2::class.java)
            startActivity(ff)
        }
    }//FIN onCREATE

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==sipermiso){
            consultarCPLlamadas()
        }
    }
    @SuppressLint("Range")
    private fun consultarCPLlamadas(){
        var resultado = ""
        text.setText("")
        val cursorLlamadas= contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,null,null,CallLog.Calls.DATE+ " DESC"
        )
        if(cursorLlamadas!!.moveToFirst()){
            do {
                // return 1 for Incoming(1), Outgoing(2) and Missed(3), 4 (VoiceMail), 5 (Rejected) and 6 (Refused List).
                var numero = cursorLlamadas.getString(cursorLlamadas.getColumnIndex(CallLog.Calls.NUMBER))
                var fecha =  cursorLlamadas.getLong(cursorLlamadas.getColumnIndex(CallLog.Calls.DATE))
                var tipo = cursorLlamadas.getInt(cursorLlamadas.getColumnIndex(CallLog.Calls.TYPE))
                var duracion = cursorLlamadas.getString(cursorLlamadas.getColumnIndex(CallLog.Calls.DURATION))
                var tipol = ""
                when(tipo){
                    CallLog.Calls.INCOMING_TYPE -> tipol="Llamada de entrada"
                    CallLog.Calls.MISSED_TYPE -> tipol="Llamada perdida"
                    CallLog.Calls.OUTGOING_TYPE -> tipol= "Llamada de salida"
                    else -> tipol = "Desconocido"
                }
                var ff = convertLongToTime(fecha)
                resultado = "Numero: ${numero}\nFecha: ${ff}\nTipo: ${tipol}\nDuracion: ${duracion}\n---------------\n"
            }while (cursorLlamadas.moveToNext())
            text.setText(resultado)
        }else{
            text.setText("NO HAY LLAMADAS")
        }
    }
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }
}