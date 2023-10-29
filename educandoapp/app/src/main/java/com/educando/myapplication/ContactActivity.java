package com.educando.myapplication;

import static com.educando.myapplication.R.id.back_account;
import static com.educando.myapplication.R.id.go_historial;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.educando.myapplication.db.DbUsuarios;

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


        // Configura el clic del botón "Enviar" para validar y enviar el mensaje
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Puedes agregar aquí la lógica de envío si es necesario
                validarYEnviarMensaje();
            }
        });

        LinearLayout cuenta = findViewById(back_account);
        cuenta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(ContactActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        // Configura el clic del botón "Volver" para volver a AccountActivity
        LinearLayout historial = findViewById(go_historial);
        historial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(ContactActivity.this, MyHistorialActivity.class);
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
            // Luego, inserta el mensaje en la base de datos
            insertPost(titulo, mensaje);
        }
    }

    private void insertPost(String titulo, String mensaje) {
        // Obtener el usuario logueado
        DbUsuarios dbUsuarios = new DbUsuarios(this);
        Usuario usuarioLogueado = dbUsuarios.obtenerUsuarioLogueado();

        if (usuarioLogueado != null) {
            // Insertar en la tabla Contacto
            long idContacto = dbUsuarios.insertarPost(usuarioLogueado.getId_usuario(), titulo, mensaje);

            if (idContacto > 0) {
                // Mensaje enviado con éxito
                mostrarMensajeEnviado();
            } else {
                // Error al insertar el mensaje
                Toast.makeText(this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
            }
        } else {
            // No se pudo obtener el usuario logueado
            Toast.makeText(this, "Usuario no logueado", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarMensajeEnviado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mensaje Enviado")
                .setMessage("Tu mensaje se ha enviado con éxito. Nos pondremos en contacto contigo pronto.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Cierra el cuadro de diálogo solo cuando el usuario hace clic en OK
                        // Después de cerrar el cuadro de diálogo, puedes continuar con la siguiente pantalla
                        Intent intent = new Intent(ContactActivity.this, AccountActivity.class);
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .show();
    }
}
