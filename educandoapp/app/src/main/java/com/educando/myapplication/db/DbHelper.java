package com.educando.myapplication.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.educando.myapplication.CategoryDomain;
import com.educando.myapplication.CourseDomain;
import com.educando.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "educando.db";
    // Nombres de las tablas
    public static final String TABLE_USUARIOS = "Usuario";  // Cambio de nombre
    public static final String TABLE_CATEGORIA = "Categoria";
    public static final String TABLE_CURSO = "Curso";
    public static final String TABLE_INTER_CUR_USER = "Inter_cur_user";

    public static final String TABLE_CONTACTO = "Contacto";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Tabla de Usuarios (ahora "usuario")
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USUARIOS + "(" +  // Cambio de nombre
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +  // Cambio de nombre
                "nombre TEXT not null," +
                "apellido TEXT not null," +
                "email TEXT not null," +
                "password TEXT not null," +
                "logueado INTEGER DEFAULT 0)");

        // Tabla de Categoría
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CATEGORIA + "(" +
                "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT)");

        // Tabla de Curso
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CURSO + "(" +
                "id_curso INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "descripcion TEXT," +
                "profesor TEXT," +
                "duracion TEXT," +
                "precio INTEGER," +
                "id_categoria INTEGER," +
                "FOREIGN KEY (id_categoria) REFERENCES " + TABLE_CATEGORIA + "(id_categoria))");

        // Tabla Intermedia entre Curso y Usuario
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_INTER_CUR_USER + "(" +
                "id_inter INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "id_curso INTEGER," +
                "es_favorito INTEGER DEFAULT 0, " +  // Nueva columna para marcar favoritos
                "FOREIGN KEY (id_usuario) REFERENCES " + TABLE_USUARIOS + "(id_usuario)," +
                "FOREIGN KEY (id_curso) REFERENCES " + TABLE_CURSO + "(id_curso))");

        // Tabla de Contacto
        sqLiteDatabase.execSQL("CREATE TABLE "  + TABLE_CONTACTO + "(" +
                "id_contact INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fecha DATE," +
                "id_usuario INTEGER," +
                "titulo TEXT(200)," +
                "mensaje TEXT(500)," +
                "FOREIGN KEY (id_usuario) REFERENCES " + TABLE_USUARIOS + "(id_usuario))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // DROP de las tablas si es necesario y crearlas nuevamente
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INTER_CUR_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTO);
        onCreate(sqLiteDatabase);
    }

    public List<CourseDomain> getAllCourses(Context context) {
        List<CourseDomain> courseList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        if (database != null) {
            Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CURSO, null);

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id_curso"));
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                    @SuppressLint("Range") String profesor = cursor.getString(cursor.getColumnIndex("profesor"));
                    @SuppressLint("Range") String duracion = cursor.getString(cursor.getColumnIndex("duracion"));
                    @SuppressLint("Range") int precio = cursor.getInt(cursor.getColumnIndex("precio"));

                    // Obtén un array de recursos de las imágenes en la carpeta "course_images"
                    TypedArray images = context.getResources().obtainTypedArray(R.array.course_images);

                    // Selecciona una imagen aleatoria
                    int randomImageIndex = (int) (Math.random() * images.length());
                    int imageResourceId = images.getResourceId(randomImageIndex, -1);

                    // Libera el array de recursos
                    images.recycle();

                    CourseDomain course = new CourseDomain(nombre, profesor, precio, duracion, context.getResources().getResourceName(imageResourceId));

                    courseList.add(course);
                } while (cursor.moveToNext());
            }

            cursor.close();
            database.close();
        } else {
            Log.e("DbHelper", "Database is null");
        }

        return courseList;
    }

    public List<CategoryDomain> getAllCategories(Context context) {
        List<CategoryDomain> categoryList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        if (database != null) {
            Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CATEGORIA, null);

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String nombreCategoria = cursor.getString(cursor.getColumnIndex("nombre"));

                    TypedArray images = context.getResources().obtainTypedArray(R.array.course_images);

                    // Selecciona una imagen aleatoria
                    int randomImageIndex = (int) (Math.random() * images.length());
                    int imageResourceId = images.getResourceId(randomImageIndex, -1);

                    // Libera el array de recursos
                    images.recycle();

                    CategoryDomain categoria = new CategoryDomain(nombreCategoria, context.getResources().getResourceName(imageResourceId));
                    categoryList.add(categoria);
                } while (cursor.moveToNext());
            }
            cursor.close();
            database.close();
        }
        return categoryList;
    }

}



