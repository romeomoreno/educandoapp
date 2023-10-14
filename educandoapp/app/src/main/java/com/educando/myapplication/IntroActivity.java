package com.educando.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.educando.myapplication.db.DataGenerator;
import com.educando.myapplication.db.DbHelper;

import java.io.File;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Nombre de la base de datos
        String nombreDeLaBaseDeDatos = "educando.db";

        // Ruta de la base de datos en la caché
        File dbFile = getDatabasePath(nombreDeLaBaseDeDatos);

        // Verificar si la base de datos ya existe en la caché
        if (dbFile.exists()) {
            Log.i("IntroActivity", "BASE DE DATOS EXISTENTE");
        } else {
            // La base de datos no existe en la caché, crea una nueva
            DbHelper dbHelper = new DbHelper(IntroActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if (db != null) {
                Log.i("IntroActivity", "BASE DE DATOS");

                // Llama al generador de datos iniciales después de crear la base de datos
                DataGenerator.generateInitialData(IntroActivity.this);

            } else {
                Log.i("IntroActivity", "ERROR AL CREAR LA BASE DE DATOS");
            }
        }

        ConstraintLayout contenedorIrLogin = findViewById(R.id.constraintLayout);
        TextView textRegistro = findViewById(R.id.textRegistro);

        contenedorIrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        textRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
