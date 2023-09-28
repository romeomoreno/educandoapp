package com.lsparda.educandoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lsparda.educandoapp.db.DbUsuarios;
public class RegisterActivity extends AppCompatActivity {

    EditText txtnombre, txtapellido, txtemail, txtpassword;
    DbUsuarios dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicialización de las vistas EditText
        txtnombre = findViewById(R.id.txtnombre);
        txtapellido = findViewById(R.id.txtapellido);
        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);
        dbUsuarios = new DbUsuarios(this);

        // Configuración del OnClickListener para el botón de registro (ImageView)
        ImageView imageView = findViewById(R.id.Register);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Registro del usuario en la base de datos SQLite
                String nombre = txtnombre.getText().toString();
                String apellido = txtapellido.getText().toString();
                String email = txtemail.getText().toString();
                String password = txtpassword.getText().toString();

                // Verifica si los campos están vacíos
                if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    // Mostrar una alerta indicando que los campos deben completarse
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Por favor, complete todos los campos.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Cierra la alerta
                        }
                    });
                    builder.show();
                } else {
                    // Todos los campos están completos, procede con el registro en la base de datos
                    long id = dbUsuarios.insertarContacto(nombre, apellido, email, password);

                    if (id != -1) {
                        // Registro exitoso en la base de datos local (SQLite)
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        // Puedes redirigir al usuario a la pantalla de inicio de sesión aquí
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Cierra esta actividad si el registro es exitoso
                    } else {
                        // Error en el registro en la base de datos local (SQLite)
                        Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    // Método opcional para borrar los campos de texto después del registro exitoso
    private void clear() {
        txtnombre.setText("");
        txtemail.setText("");
        txtpassword.setText("");
    }
}
