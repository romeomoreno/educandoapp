package com.educando.myapplication;

import static com.educando.myapplication.db.DbHelper.TABLE_CATEGORIA;
import static com.educando.myapplication.db.DbHelper.TABLE_CURSO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.educando.myapplication.db.DbHelper;
import com.educando.myapplication.db.DbUsuarios;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;

public class CategoryCourseActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Course> courseList;
    CourseAdapter courseAdapter;
    String categoryName; // El nombre de la categoría seleccionada
    DbUsuarios dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cour_of_cat);

        // Recupera el nombre de la categoría seleccionada (puedes pasarla desde la actividad anterior)
        categoryName = getIntent().getStringExtra("categoryName");

        // Obtén el TextView para mostrar el nombre de la categoría
        TextView categoryNameTextView = findViewById(R.id.list_course);
        // Establece el nombre de la categoría en el TextView
        categoryNameTextView.setText(categoryName);

        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Agrega el sombreado inferior
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Inicializa la lista de cursos
        courseList = new ArrayList<>();

        // Inicializa el adaptador
        courseAdapter = new CourseAdapter(courseList);

        // Configura el adaptador en el RecyclerView
        recyclerView.setAdapter(courseAdapter);

        // Inicializa la instancia de DbUsuarios
        dbUsuarios = new DbUsuarios(this);

        // Obtiene el usuario logueado desde la base de datos local (SQLite)
        Usuario usuario = dbUsuarios.obtenerUsuarioLogueado();

        // Obtén y muestra los cursos de la categoría seleccionada
        obtenerCursosDeCategoria(categoryName);

        LinearLayout inicio = findViewById(R.id.back_main);

        inicio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(CategoryCourseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout cuenta = findViewById(R.id.back_account);
        cuenta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Aquí escribirás el código para iniciar la Main Activity
                Intent intent = new Intent(CategoryCourseActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void obtenerCursosDeCategoria(String categoryName) {
        // Inicializa la lista de cursos
        courseList.clear();

        // Obtén una instancia de la base de datos SQLite
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        if (database != null) {
            // Realiza una consulta SQL para obtener los cursos de la categoría
            String query = "SELECT * FROM " + TABLE_CURSO +
                    " WHERE " + TABLE_CURSO + ".id_categoria = " +
                    "(SELECT id_categoria FROM " + TABLE_CATEGORIA +
                    " WHERE nombre = ?)";

            Cursor cursor = database.rawQuery(query, new String[]{categoryName});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Obtén los detalles del curso desde el cursor
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                    // Agrega el curso a la lista
                    Course course = new Course(nombre, descripcion);
                    courseList.add(course);
                } while (cursor.moveToNext());

                // Notifica al adaptador que los datos han cambiado
                courseAdapter.notifyDataSetChanged();

                // Cierra el cursor
                cursor.close();
            } else {
                // Manejo de caso en el que no se encontraron cursos en la categoría
                Toast.makeText(this, "No se encontraron cursos en la categoría seleccionada.", Toast.LENGTH_SHORT).show();
            }

            // Cierra la conexión a la base de datos
            database.close();
        } else {
            // Manejo de caso en el que no se puede abrir la base de datos
            Toast.makeText(this, "Error al abrir la base de datos.", Toast.LENGTH_SHORT).show();
        }
    }

    // Adaptador para la lista de cursos
    private class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

        private List<Course> courses;

        public CourseAdapter(List<Course> courses) {
            this.courses = courses;
        }

        @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_course, parent, false);
            return new CourseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            Course course = courses.get(position);
            holder.bind(course, position);
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }

        // ViewHolder para un curso
        public class CourseViewHolder extends RecyclerView.ViewHolder {

            private TextView courseNameTextView;
            private TextView descriptionTextView;
            private ImageView courseImageView;
            private Button ButtomModifi;

            public CourseViewHolder(View itemView) {
                super(itemView);
                courseNameTextView = itemView.findViewById(R.id.course_name);
                descriptionTextView = itemView.findViewById(R.id.course_description);
                courseImageView = itemView.findViewById(R.id.course_image);
                ButtomModifi = itemView.findViewById(R.id.ButtomModifi);
                ButtomModifi.setText("+");
            }

            public void bind(Course course, int position) {
                courseNameTextView.setText(course.getName());
                descriptionTextView.setText(course.getDescription());

                ButtomModifi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agregarCursoAlUsuario(position);
                    }
                });
            }
        }

        // Método para agregar cursos al usuario en la base de datos
        private void agregarCursoAlUsuario(int position) {
            if (position >= 0 && position < courseList.size()) {
                Course course = courseList.get(position);
                int courseId = obtenerIdCursoDesdeNombre(course.getName());

                if (courseId != -1) {
                    // Obtiene al usuario logueado
                    Usuario usuario = dbUsuarios.obtenerUsuarioLogueado();

                    if (usuario != null) {
                        SQLiteDatabase database = dbUsuarios.getWritableDatabase();

                        if (database != null) {
                            // Define los valores a insertar en la tabla de asignaciones
                            ContentValues values = new ContentValues();
                            values.put("id_usuario", usuario.getId_usuario());
                            values.put("id_curso", courseId);

                            // Inserta el nuevo registro en la tabla de asignaciones
                            long newRowId = database.insert(DbHelper.TABLE_INTER_CUR_USER, null, values);

                            if (newRowId != -1) {
                                Toast.makeText(CategoryCourseActivity.this, "Curso agregado exitosamente.", Toast.LENGTH_SHORT).show();
                            }
                            database.close();
                        }
                    }
                }
            }
        }
        @SuppressLint("Range")
        private int obtenerIdCursoDesdeNombre(String nombreCurso) {
            int courseId = -1;
            SQLiteDatabase database = dbUsuarios.getReadableDatabase();

            if (database != null) {
                String query = "SELECT id_curso FROM " + DbHelper.TABLE_CURSO + " WHERE nombre = ?";
                Cursor cursor = database.rawQuery(query, new String[]{nombreCurso});

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        courseId = cursor.getInt(cursor.getColumnIndex("id_curso"));
                    }
                    cursor.close();
                }
                database.close();
            }
            return courseId;
        }
    }
}
