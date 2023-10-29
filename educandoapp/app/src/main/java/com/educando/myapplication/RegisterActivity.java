package com.educando.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.educando.myapplication.db.DbUsuarios;
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

                // Expresiones regulares para validar nombre, apellido y email
                String nombreRegex = "^[A-Za-z]+$"; // Solo letras
                String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"; // Formato de email

                // Verifica si los campos están vacíos
                if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                } else if (!nombre.matches(nombreRegex) || !apellido.matches(nombreRegex)) {
                    // Validación para nombre y apellido
                    Toast.makeText(RegisterActivity.this, "Nombre y Apellido deben contener solo letras.", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailRegex)) {
                    // Validación para el formato de email
                    Toast.makeText(RegisterActivity.this, "El correo electrónico no tiene un formato válido.", Toast.LENGTH_SHORT).show();
                } else {
                    // Verifica si el correo electrónico ya existe en la base de datos
                    if (dbUsuarios.existeUsuario(email)) {
                        // El correo electrónico ya está registrado, muestra un mensaje de error mediante un Toast
                        Toast.makeText(RegisterActivity.this, "El correo electrónico ya está registrado.", Toast.LENGTH_SHORT).show();

                    } else {
                        // El correo electrónico no existe en la base de datos, procede con el registro
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