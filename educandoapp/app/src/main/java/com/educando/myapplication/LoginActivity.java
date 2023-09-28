package com.educando.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.educando.myapplication.db.DbUsuarios;

public class LoginActivity extends AppCompatActivity {

    EditText txtemail, txtpassword;
    DbUsuarios dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtemail = findViewById(R.id.emailLogin);
        txtpassword = findViewById(R.id.passwordLogin);
        dbUsuarios = new DbUsuarios(this);

        // Configurar un OnClickListener para el TextView con id @+id/registerLogin
        TextView registerTextView = findViewById(R.id.registerLogin1);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad RegisterActivity cuando se hace clic en el TextView
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageButton = findViewById(R.id.Login);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtemail.getText().toString();
                final String password = txtpassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    // Muestra un mensaje de error si alguno de los campos está en blanco
                    Log.i("LoginActivity", "Ingresa email y contraseña");
                    return; // Sale de la función si los campos están en blanco
                }

                // Realiza la autenticación en la base de datos local SQLite
                boolean loggedIn = dbUsuarios.buscarUsuario(email, password);

                if (loggedIn) {
                    // El inicio de sesión fue exitoso en la base de datos local (SQLite)

                    // Actualiza el campo logueado a 1 (verdadero) para el usuario actual
                    boolean actualizado = dbUsuarios.actualizarEstadoLogueado(email, 1);

                    if (actualizado) {
                        Log.i("LoginActivity", "Inicio de sesión exitoso");

                        // Después de mostrar el mensaje de bienvenida, puedes iniciar la MainActivity u otras acciones necesarias
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.i("LoginActivity", "Error al actualizar el estado de inicio de sesión");
                    }
                } else {
                    // No se encontró un usuario con las credenciales proporcionadas en la base de datos local (SQLite)
                    Log.i("LoginActivity", "Credenciales incorrectas, intenta de nuevo");
                }
            }
        });
    }
}

