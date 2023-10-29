package com.educando.myapplication;

import static com.educando.myapplication.R.id.back_main;
import static com.educando.myapplication.R.id.back_account;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    //Obtiene una lista de cursos asociados a un usuario específico desde la base de datos local.
    private List<Course> obtenerCursosDeUsuario(int idUsuario) {
        // Inicializa una lista vacía para almacenar los cursos del usuario
        List<Course> courses = new ArrayList<>();

        // Obtiene una instancia de la base de datos en modo lectura
        SQLiteDatabase database = dbUsuarios.getReadableDatabase();

        // Verifica si la base de datos es accesible
        if (database != null) {
            // Construye una consulta SQL para obtener los cursos del usuario
            String query = "SELECT c.* FROM " + DbHelper.TABLE_CURSO + " c " +
                    "INNER JOIN " + DbHelper.TABLE_INTER_CUR_USER + " i " +
                    "ON c.id_curso = i.id_curso " +
                    "WHERE i.id_usuario = ?";

            // Ejecuta la consulta con el ID del usuario como parámetro
            Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(idUsuario)});

            // Verifica si se obtuvieron resultados en la consulta
            if (cursor != null) {
                // Itera a través de los resultados del cursor
                while (cursor.moveToNext()) {
                    // Obtiene el nombre y la descripción del curso desde el cursor
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));

                    // Crea un objeto Course y agrégalo a la lista
                    Course course = new Course(nombre, descripcion);

                    // Agrega el curso a la lista de cursos del usuario
                    courses.add(course);
                }

                // Cierra el cursor
                cursor.close();
            }

            // Cierra la conexión a la base de datos en modo lectura
            database.close();
        }

        // Devuelve la lista de cursos del usuario
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
            private ImageButton favoriteButton;

            private Button buttonCompra;

            public CourseViewHolder(View itemView) {
                super(itemView);
                courseNameTextView = itemView.findViewById(R.id.course_name);
                descriptionTextView = itemView.findViewById(R.id.course_description);
                courseImageView = itemView.findViewById(R.id.course_image);
                favoriteButton = itemView.findViewById(R.id.favoriteButton);
                favoriteButton.setImageResource(R.drawable.delete);
                buttonCompra = itemView.findViewById(R.id.button_compra);
            }

            public void bind(Course course, int position) {
                courseNameTextView.setText(course.getName());
                descriptionTextView.setText(course.getDescription());
                // Obtener una imagen aleatoria para cada curso
                int randomImageIndex = new Random().nextInt(courseImages.length());
                int resourceId = courseImages.getResourceId(randomImageIndex, -1);

                if (resourceId != -1) {
                    courseImageView.setImageResource(resourceId);
                }

                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Mostrar un diálogo de confirmación antes de eliminar el curso
                        showConfirmationDialog(position);
                    }
                });

                // Configura la lógica del botón de compra aquí
                buttonCompra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lógica de tu botón, por ejemplo, abrir una URL
                        String url = "https://www.google.com";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        Log.d("MiApp", "Se hizo clic en el botón de compra");

                        if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                            v.getContext().startActivity(intent);
                        } else {
                            Toast.makeText(v.getContext(), "No se puede abrir la URL. Instala un navegador web.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        // Método para eliminar cursos asignados al usuario de la base de datos
        private void eliminarCursoDeBaseDeDatos(int position) {
            if (position >= 0 && position < courseList.size()) {
                Course course = courseList.get(position);
                int courseId = obtenerIdCursoDesdeNombre(course.getName());

                if (courseId != -1) {
                    // Obtiene al usuario logueado
                    Usuario usuario = dbUsuarios.obtenerUsuarioLogueado();

                    if (usuario != null) {
                        SQLiteDatabase database = dbUsuarios.getWritableDatabase();

                        if (database != null) {
                            // Define la cláusula WHERE para eliminar la asignación del curso con el ID correspondiente
                            String whereClause = "id_curso = ? AND id_usuario = ?";
                            String[] whereArgs = {String.valueOf(courseId), String.valueOf(usuario.getId_usuario())};

                            // Ejecuta la sentencia SQL DELETE en la tabla de asignaciones
                            int rowsDeleted = database.delete(DbHelper.TABLE_INTER_CUR_USER, whereClause, whereArgs);

                            if (rowsDeleted > 0) {
                                // Eliminación exitosa, ahora elimina el curso visualmente y de la lista
                                courseList.remove(position);
                                courseAdapter.notifyItemRemoved(position);
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

        // Método para mostrar un diálogo de confirmación
        private void showConfirmationDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyCourseActivity.this);
            builder.setMessage("¿Estás seguro de que deseas eliminar este curso de tus favoritos?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    eliminarCursoDeBaseDeDatos(position);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}