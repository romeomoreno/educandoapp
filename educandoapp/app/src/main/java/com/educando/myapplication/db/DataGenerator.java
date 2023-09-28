package com.lsparda.educandoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Random;

public class DataGenerator {

    // Método para generar datos iniciales en la base de datos
    public static void generateInitialData(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Generar categorías de cursos
        generateCategories(database);

        // Generar cursos
        generateCourses(database);

        // Crear un usuario y asignarle 3 cursos
        createUserAndAssignCourses(database);

        // Cierra la base de datos
        database.close();
    }

    private static void generateCategories(SQLiteDatabase database) {
        // Nombre de las categorías de ejemplo
        String[] categoryNames = {"Programación Java", "Desarrollo Android", "Diseño de UI"};

        for (String categoryName : categoryNames) {
            ContentValues values = new ContentValues();
            values.put("nombre", categoryName);
            database.insert(DbHelper.TABLE_CATEGORIA, null, values);
        }
    }

    private static void generateCourses(SQLiteDatabase database) {
        // Nombre de cursos de ejemplo
        String[] courseNames = {"Curso de Java Avanzado", "Desarrollo de Aplicaciones Android", "Diseño de Interfaces de Usuario", "Python"};
        Random random = new Random();

        for (String courseName : courseNames) {
            ContentValues values = new ContentValues();
            values.put("nombre", courseName);
            values.put("descripcion", generateRandomDescription(courseName)); // Generar descripción aleatoria
            values.put("profesor", generateRandomProfessor(courseName)); // Generar nombre de profesor aleatorio
            values.put("duracion", "8 semanas"); // Duración ficticia
            int precio = random.nextInt(501) + 500; // Precio aleatorio entre 500 y 1000
            values.put("precio", precio); // Agregar el precio al ContentValues
            values.put("id_categoria", random.nextInt(3) + 1); // Asigna a una categoría aleatoria
            database.insert(DbHelper.TABLE_CURSO, null, values);
        }
    }

    private static String generateRandomDescription(String courseName) {
        // Utiliza un switch o una serie de if-else para asignar descripciones diferentes
        // basadas en el nombre del curso
        switch (courseName) {
            case "Curso de Java Avanzado":
                return "Conoce los conceptos clave del lenguaje de programación que se está comiendo al mundo. Aprende qué es una variable, una función, un objeto y dónde se guardan esos valores";
            case "Desarrollo de Aplicaciones Android":
                return "Sé parte de la comunidad que ya se está preparando para los desafíos de la IA.";
            case "Diseño de Interfaces de Usuario":
                return "El diseñador UX se centra en investigar y analizar las necesidades del cliente";
            case "Python":
                return "En este curso aprenderás las bases de programación uno de los lenguajes más populares en estos tiempos. Partirás desde sus fundamentos, para luego abarcar módulos y sintaxis, hasta el uso de reglas para crear tus primeras aplicaciones.";
            default:
                return "Descripción genérica para otros cursos.";
        }
    }

    private static String generateRandomProfessor(String courseName) {
        // Generar un nombre de profesor aleatorio basado en el nombre del curso
        String[] professorNames = {"Profesor Ramiro", "Profesor Carlos", "Profesor Viviana"};
        Random random = new Random();
        int index = random.nextInt(professorNames.length);
        return professorNames[index];
    }

    private static String getRandomLoremIpsum() {
        // Esto es solo un ejemplo de cómo generar texto aleatorio (lorem ipsum)
        return "Aprende con los mejores profesores en la materia.";
    }

    private static void createUserAndAssignCourses(SQLiteDatabase database) {
        // Crear un nuevo usuario en la tabla Usuarios
        ContentValues usuarioValues = new ContentValues();
        usuarioValues.put("nombre", "Romeo");
        usuarioValues.put("apellido", "Moreno");
        usuarioValues.put("email", "romeo@gmail.com");
        usuarioValues.put("password", "romeo123");
        usuarioValues.put("logueado", 0); // O el valor que desees
        long usuarioId = database.insert(DbHelper.TABLE_USUARIOS, null, usuarioValues);

        // Asignar 3 cursos aleatorios al usuario
        int numCursos = 3; // Supongamos que tienes 3 cursos en total
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int courseId = random.nextInt(numCursos) + 1; // ID de curso aleatorio
            ContentValues values = new ContentValues();
            values.put("id_usuario", usuarioId);
            values.put("id_curso", courseId);
            database.insert(DbHelper.TABLE_INTER_CUR_USER, null, values);
        }
    }
}
