package com.educando.myapplication;


import static com.educando.myapplication.R.id.acc_go_main;
import static com.educando.myapplication.R.id.acc_go_cursos;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.educando.myapplication.db.DbHelper;
import com.educando.myapplication.db.DbUsuarios;

public class AccountActivity extends AppCompatActivity {

    TextView nombreTextView, apellidoTextView, emailTextView, cursosTextView, changePassButton;
    Button disconectButton;
    DbUsuarios dbUsuarios;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        nombreTextView = findViewById(R.id.acc_nombre_b);
        apellidoTextView = findViewById(R.id.acc_apellido_b);
        emailTextView = findViewById(R.id.acc_email_b);
        cursosTextView = findViewById(R.id.acc_curso_b);
        dbUsuarios = new DbUsuarios(this);
        dbHelper = new DbHelper(this);

        // Obtén el usuario logueado desde la base de datos local (SQLite)
        Usuario usuario = dbUsuarios.obtenerUsuarioLogueado();

        if (usuario != null) {
            // Los datos del usuario están disponibles
            String nombre = usuario.getNombre();
            String apellido = usuario.getApellido();
            final int idUsuario = usuario.getId_usuario();

            nombreTextView.setText(nombre);
            apellidoTextView.setText(apellido);
            emailTextView.setText(usuario.getEmail());

            // Obtén la cantidad de cursos del usuario desde la base de datos usando el id_usuario
            int cantidadCursos = obtenerCantidadDeCursos(idUsuario);
            cursosTextView.setText("Cantidad de cursos: " + cantidadCursos);

            // Configura el OnClickListener para el botón de cambiar contraseña
            changePassButton = findViewById(R.id.change_pass);
            changePassButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redirige al usuario a la actividad ChangePassActivity
                    Intent intent = new Intent(AccountActivity.this, ChangePassActivity.class);
                    startActivity(intent);
                }
            });

            // Configura el OnClickListener para el botón "Desconectar" (nuevo botón)
            disconectButton = findViewById(R.id.disconect);
            disconectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Actualiza el estado de logueado a 0 (falso) al desconectar
                    boolean actualizado = dbUsuarios.actualizarEstadoLogueado(usuario.getEmail(), 0);

                    if (actualizado) {
                        // Redirigir al usuario a la actividad IntroActivity
                        Intent intent = new Intent(AccountActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish(); // Cierra esta actividad si el usuario se desconecta
                    } else {
                        Toast.makeText(AccountActivity.this, "Error al actualizar el estado de inicio de sesión", Toast.LENGTH_LONG).show();
                    }
                }
            });

            // Configura el OnClickListener para el botón "Contacto" (nuevo botón)
            LinearLayout contactButton = findViewById(R.id.contact);
            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redirige al usuario a la actividad ContactActivity
                    Intent intent = new Intent(AccountActivity.this, ContactActivity.class);
                    startActivity(intent);
                }
            });

            LinearLayout inicio = findViewById(acc_go_main);
            inicio.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Aquí escribirás el código para iniciar la Main Activity
                    Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            });

            LinearLayout cursos = findViewById(acc_go_cursos);
            cursos.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Aquí escribirás el código para iniciar la Main Activity
                    Intent intent = new Intent(AccountActivity.this, MyCourseActivity.class);
                    startActivity(intent);

                }
            });

            // Obtén una referencia al botón "Abrir URL"
            LinearLayout openUrlButton = findViewById(R.id.buy_course);

            // Configura un oyente de clic para el botón
            openUrlButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Define la URL que deseas abrir
                    String url = "https://www.google.com"; // Reemplaza con tu URL real

                    // Crea un intent para abrir la URL en un navegador web
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    // Verifica si hay una aplicación para manejar la acción de abrir la URL
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Abre la URL en un navegador web
                        startActivity(intent);
                    } else {
                        // No se encontró una aplicación para manejar la acción
                        // Puedes mostrar un mensaje de error o manejarlo como prefieras
                        Toast.makeText(AccountActivity.this, "No se puede abrir la URL. Instala un navegador web.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            // No se encontró un usuario logueado, puedes manejarlo como prefieras
            // Por ejemplo, redirigiendo a la pantalla de inicio de sesión
            Toast.makeText(AccountActivity.this, "Usuario no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private int obtenerCantidadDeCursos(int idUsuario) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int cantidad = 0;

        if (database != null) {
            // Realiza una consulta para contar la cantidad de cursos asignados al usuario
            String query = "SELECT COUNT(*) FROM " + DbHelper.TABLE_INTER_CUR_USER + " WHERE id_usuario = ?";
            Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(idUsuario)});

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    cantidad = cursor.getInt(0);
                }
                cursor.close();
            }

            database.close();
        }

        return cantidad;
    }
}
