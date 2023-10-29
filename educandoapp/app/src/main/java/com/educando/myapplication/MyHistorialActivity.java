package com.educando.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.educando.myapplication.db.DbUsuarios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MyHistorialActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MensajesAdapter mensajesAdapter;
    // Crea una lista vacía de mensajes al inicio de tu actividad
    private List<Contacto> mensajesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        recyclerView = findViewById(R.id.recycler_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa el adaptador pasando la lista de mensajes
        mensajesAdapter = new MensajesAdapter(mensajesList);
        recyclerView.setAdapter(mensajesAdapter);

        // Carga los mensajes en un hilo aparte
        loadMensajesEnviados();

        LinearLayout cuenta = findViewById(R.id.back_account);
        cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(MyHistorialActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout post = findViewById(R.id.back_post1);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(MyHistorialActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadMensajesEnviados() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Obtén los mensajes enviados desde la base de datos
                DbUsuarios dbUsuarios = new DbUsuarios(MyHistorialActivity.this);
                final List<Contacto> mensajes = dbUsuarios.getMensajesEnviados();

                // Ordena los mensajes en orden descendente por fecha
                Collections.sort(mensajes, new Comparator<Contacto>() {
                    @Override
                    public int compare(Contacto mensaje1, Contacto mensaje2) {
                        // Compara las fechas en orden descendente
                        return mensaje2.getFecha().compareTo(mensaje1.getFecha());
                    }
                });

                // Actualiza la vista en el hilo principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mensajesList.clear(); // Limpia la lista actual
                        mensajesList.addAll(mensajes); // Agrega los mensajes ordenados
                        mensajesAdapter.notifyDataSetChanged(); // Notifica al adaptador del cambio
                    }
                });
            }
        }).start();
    }


}

