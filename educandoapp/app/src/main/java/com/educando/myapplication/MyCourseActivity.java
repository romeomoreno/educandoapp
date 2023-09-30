package com.educando.myapplication;

import static com.educando.myapplication.R.id.back_main;
import static com.educando.myapplication.R.id.back_account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Random;

import androidx.recyclerview.widget.DividerItemDecoration;

public class MyCourseActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Course> courseList;
    CourseAdapter courseAdapter;
    DbUsuarios dbUsuarios;
    TypedArray courseImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.recycler_view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Agrega el sombreado inferior
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Deshabilita clipToPadding para evitar el espaciado no deseado
        recyclerView.setClipToPadding(false);

        // Inicializa la lista de cursos
        courseList = new ArrayList<>();

        // Inicializa el adaptador
        courseAdapter = new CourseAdapter(courseList);

        // Configura el adaptador en el RecyclerView
        recyclerView.setAdapter(courseAdapter);

        // Inicializa la instancia de DbUsuarios
        dbUsuarios = new DbUsuarios(this);

        // Inicializa el TypedArray para obtener imágenes aleatorias
        courseImages = getResources().obtainTypedArray(R.array.course_images);


        // Obtiene el usuario logueado desde la base de datos local (SQLite)
        Usuario usuario = dbUsuarios.obtenerUsuarioLogueado();

        if (usuario != null) {
            // Los datos del usuario están disponibles
            // Obtén la lista de cursos del usuario desde la base de datos local
            List<Course> userCourses = obtenerCursosDeUsuario(usuario.getId_usuario());

            if (userCourses != null && !userCourses.isEmpty()) {
                // Agrega los cursos del usuario a la lista
                courseList.addAll(userCourses);

                // Notifica al adaptador que los datos han cambiado
                courseAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "El usuario no tiene cursos.", Toast.LENGTH_SHORT).show();
            }

            LinearLayout inicio = findViewById(back_main);

            inicio.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Aquí escribirás el código para iniciar la Main Activity
                    Intent intent = new Intent(MyCourseActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            });

            LinearLayout cuenta = findViewById(back_account);
            cuenta.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Aquí escribirás el código para iniciar la Main Activity
                    Intent intent = new Intent(MyCourseActivity.this, AccountActivity.class);
                    startActivity(intent);

                }
            });
        } else {
            // No se encontró un usuario logueado, puedes manejarlo como prefieras
            // Por ejemplo, redirigiendo a la pantalla de inicio de sesión
            Toast.makeText(this, "Usuario no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MyCourseActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private List<Course> obtenerCursosDeUsuario(int idUsuario) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase database = dbUsuarios.getReadableDatabase();

        if (database != null) {
            String query = "SELECT c.* FROM " + DbHelper.TABLE_CURSO + " c " +
                    "INNER JOIN " + DbHelper.TABLE_INTER_CUR_USER + " i " +
                    "ON c.id_curso = i.id_curso " +
                    "WHERE i.id_usuario = ?";
            Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(idUsuario)});

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));

                    // Crea un objeto Course y agrégalo a la lista
                    Course course = new Course(nombre, descripcion);
                    courses.add(course);
                }

                cursor.close();
            }

            database.close();
        }

        return courses;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
            return new CourseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            Course course = courses.get(position);
            holder.bind(course);
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

            public CourseViewHolder(View itemView) {
                super(itemView);
                courseNameTextView = itemView.findViewById(R.id.course_name);
                descriptionTextView = itemView.findViewById(R.id.course_description);
                courseImageView = itemView.findViewById(R.id.course_image);
            }
            public void bind(Course course) {
                courseNameTextView.setText(course.getName());
                descriptionTextView.setText(course.getDescription());
                // Obtener una imagen aleatoria para cada curso
                int randomImageIndex = new Random().nextInt(courseImages.length());
                int resourceId = courseImages.getResourceId(randomImageIndex, -1);

                if (resourceId != -1) {
                    courseImageView.setImageResource(resourceId);
                }
            }
        }
    }
}
