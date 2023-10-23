package com.educando.myapplication;

import static com.educando.myapplication.R.id.back_account;
import static com.educando.myapplication.R.id.back_post1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.educando.myapplication.db.DbUsuarios;

import java.util.List;


public class MyHistorialActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MensajesAdapter mensajesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        recyclerView = findViewById(R.id.recycler_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa el adaptador
        mensajesAdapter = new MensajesAdapter();
        recyclerView.setAdapter(mensajesAdapter);

        // Carga los mensajes en un hilo aparte
        loadMensajesEnviados();

        LinearLayout cuenta = findViewById(back_account);
        cuenta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(MyHistorialActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout post = findViewById(back_post1);

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

                // Actualiza la vista en el hilo principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mensajesAdapter.setMensajes(mensajes);
                    }
                });
            }
        }).start();
    }
}

