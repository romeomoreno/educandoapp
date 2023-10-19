package com.educando.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.educando.myapplication.db.DbUsuarios;

public class ChangePassActivity extends AppCompatActivity {

    EditText oldPasswordEditText, newPassword1EditText, newPassword2EditText;
    Button changePassword;
    DbUsuarios dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordEditText = findViewById(R.id.oldpass);
        newPassword1EditText = findViewById(R.id.txtchangepass1);
        newPassword2EditText = findViewById(R.id.txtchangepass2);
        changePassword = findViewById(R.id.changePassword);
        dbUsuarios = new DbUsuarios(this);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener contraseñas ingresadas por el usuario
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword1 = newPassword1EditText.getText().toString();
                String newPassword2 = newPassword2EditText.getText().toString();

                // Validar que las nuevas contraseñas coincidan
                if (!newPassword1.equals(newPassword2)) {
                    Toast.makeText(ChangePassActivity.this, "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar que la contraseña antigua sea correcta
                Usuario usuario = dbUsuarios.obtenerUsuarioLogueado();
                if (usuario != null) {
                    // Encriptar la contraseña antigua ingresada por el usuario
                    String hashedOldPassword = dbUsuarios.hashPassword(oldPassword);

                    // Comparar la contraseña encriptada almacenada en la base con la contraseña antigua encriptada
                    if (hashedOldPassword.equals(usuario.getPassword())) {
                        // Encriptar la nueva contraseña
                        String hashedNewPassword = dbUsuarios.hashPassword(newPassword1);

                        // Cambiar la contraseña encriptada
                        boolean actualizado = dbUsuarios.actualizarPassword(usuario.getId_usuario(), hashedNewPassword);

                        if (actualizado) {
                            Toast.makeText(ChangePassActivity.this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();

                            // Crear un intent para ir a AccountActivity
                            Intent intent = new Intent(ChangePassActivity.this, AccountActivity.class);
                            startActivity(intent);

                            // Cerrar esta actividad
                            finish();
                        } else {
                            Toast.makeText(ChangePassActivity.this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePassActivity.this, "La contraseña antigua es incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
