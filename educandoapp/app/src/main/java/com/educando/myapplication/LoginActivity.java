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
                    Log.i("LoginActivity", "Ingresa email y contraseña");
                    mostrarMensajeError("Ingresa email y contraseña");
                    return;
                }

                // Obtén la contraseña encriptada de la base de datos
                String hashedPassword = dbUsuarios.obtenerPassword(email);

                // Hashea la contraseña ingresada
                String hashedInputPassword = dbUsuarios.hashPassword(password);

                if (hashedPassword != null && hashedPassword.equals(hashedInputPassword)) {
                    // La contraseña coincide, lo que significa un inicio de sesión exitoso
                    boolean actualizado = dbUsuarios.actualizarEstadoLogueado(email, 1);

                    if (actualizado) {
                        Log.i("LoginActivity", "Inicio de sesión exitoso");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.i("LoginActivity", "Error al actualizar el estado de inicio de sesión");
                    }
                } else {
                    mostrarMensajeError("Usuario o contraseña incorrectos.");
                }
            }
        });
    }

    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }
}