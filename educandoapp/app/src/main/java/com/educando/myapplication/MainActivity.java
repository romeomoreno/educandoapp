package com.educando.myapplication;

import static com.educando.myapplication.R.id.Cuenta;
import static com.educando.myapplication.R.id.Mycourse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.educando.myapplication.db.DbHelper;
import com.educando.myapplication.db.DbUsuarios;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adpterCourses;
    private RecyclerView.Adapter categoryAdapter;
    private TextView nombre_main;
    public RecyclerView recyclerViewCategory;
    public RecyclerView recyclerViewCourse;
    private DbHelper dbHelper;
    private DbUsuarios dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        dbUsuarios = new DbUsuarios(this); // Inicializa DbUsuarios

        initRecyclerView();
        setupCategoryRecyclerView();

        // Obtén una referencia al TextView nombre_main
        nombre_main = findViewById(R.id.nombre_main);

        LinearLayout cuenta = findViewById(Cuenta);
        LinearLayout miscursos = findViewById(Mycourse);

        // Aquí obtén el nombre del usuario logueado, por ejemplo, de tu sistema de autenticación
        Usuario nombreUsuario = dbUsuarios.obtenerUsuarioLogueado();

        if (nombreUsuario != null) {
            // Establece el nombre del usuario en el TextView
            String nombreCompleto = nombreUsuario.getNombre() + " " + nombreUsuario.getApellido();
            nombre_main.setText(nombreCompleto);
        }

        cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        miscursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyCourseActivity.class);
                startActivity(intent);
            }
        });

        Button openUrlButton = findViewById(R.id.button_compra);

        openUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.google.com";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No se puede abrir la URL. Instala un navegador web.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerViewCourse = findViewById(R.id.view);
        recyclerViewCourse.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Obtener los cursos desde la base de datos
        ArrayList<CourseDomain> courseList = (ArrayList<CourseDomain>) dbHelper.getAllCourses(this);

        // Inicializar el adaptador con la lista de cursos
        adpterCourses = new CourseAdapter(courseList);
        recyclerViewCourse.setAdapter(adpterCourses);
    }

    private void setupCategoryRecyclerView() {
        recyclerViewCategory = findViewById(R.id.recycler_view_categories);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Obtener las categorías desde el array
        ArrayList<CategoryDomain> categoryList = (ArrayList<CategoryDomain>) dbHelper.getAllCategories(this);

        // Inicializar el adaptador con el array de categorías y el contexto
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList);
        recyclerViewCategory.setAdapter(categoryAdapter);
    }


}