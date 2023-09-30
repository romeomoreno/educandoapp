package com.educando.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Obtén referencias a los botones
        Button enviarButton = findViewById(R.id.btnEnviar);
        Button volverButton = findViewById(R.id.btnBack);

        // Configura el clic del botón "Enviar" para volver a AccountActivity
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Puedes agregar aquí la lógica de envío si es necesario
                // Luego, inicia la actividad AccountActivity
                Intent intent = new Intent(ContactActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        // Configura el clic del botón "Volver" para volver a AccountActivity
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad AccountActivity
                Intent intent = new Intent(ContactActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
