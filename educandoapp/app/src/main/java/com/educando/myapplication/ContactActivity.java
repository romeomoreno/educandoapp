package com.educando.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    private EditText txttitle;
    private EditText txtBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Obtén referencias a los campos
        txttitle = findViewById(R.id.txttitle);
        txtBlock = findViewById(R.id.txtBlock);

        // Obtén referencias a los botones
        Button enviarButton = findViewById(R.id.btnEnviar);
        Button volverButton = findViewById(R.id.btnBack);

        // Configura el clic del botón "Enviar" para volver a AccountActivity
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Puedes agregar aquí la lógica de envío si es necesario
                validarYEnviarMensaje();
                // Luego, inicia la actividad AccountActivity
                //Intent intent = new Intent(ContactActivity.this, AccountActivity.class);
                // startActivity(intent);
            }
        });

        // Configura el clic del botón "Volver" para volver a AccountActivity
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad AccountActivity
                Intent intent = new Intent(ContactActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validarYEnviarMensaje() {
        String titulo = txttitle.getText().toString();
        String mensaje = txtBlock.getText().toString();

        if (titulo.isEmpty() || mensaje.isEmpty()) {
            // Mostrar una alerta si alguno de los campos está vacío
            Toast.makeText(ContactActivity.this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Realizar el envío del mensaje aquí, si es necesario
            mostrarMensajeEnviado();
            // Puedes agregar la lógica para enviar el mensaje a través de una API o cualquier otra acción
        }
    }

    private void mostrarMensajeEnviado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mensaje Enviado")
                .setMessage("Tu mensaje se ha enviado con éxito. Nos pondremos en contacto contigo pronto.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Aquí puedes realizar cualquier acción adicional si es necesario
                        dialog.dismiss(); // Cierra el cuadro de diálogo solo cuando el usuario hace clic en OK
                        // Después de cerrar el cuadro de diálogo, puedes continuar con la siguiente pantalla
                        Intent intent = new Intent(ContactActivity.this, AccountActivity.class);
                        startActivity(intent);
                    }
                })
                .setCancelable(false) // Evita que el usuario cierre el cuadro de diálogo tocando fuera de él
                .show();
    }

}
