package com.educando.myapplication.db;

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

        String[] courseNames = {"Curso de Java Avanzado", "Desarrollo de Aplicaciones Android",
                "Diseño de Interfaces de Usuario", "Master Completo en Java de cero a experto",
                "Introducción a la programación en Android","Curso de diseño de experiencia de usuario e interacciones"};

        for (String courseName : courseNames) {
            ContentValues values = new ContentValues();
            values.put("nombre", courseName);
            values.put("descripcion", generateRandomDescription(courseName));
            values.put("profesor", generateRandomProfessor(courseName));
            int duracionSemanas = new Random().nextInt(9) + 4;
            values.put("duracion", duracionSemanas + " semanas");
            int precio = new Random().nextInt(501) + 500;
            values.put("precio", precio);

            // Utilizar expresiones regulares para detectar palabras clave en el nombre del curso
            if (courseName.matches("(?i).*\\bjava\\b.*")) {
                values.put("id_categoria", 1);
            } else if (courseName.matches("(?i).*\\bandroid\\b.*")) {
                values.put("id_categoria", 2);
            } else if (courseName.matches("(?i).*\\bdiseño\\b.*")) {
                values.put("id_categoria", 3);
            } else {
                values.put("id_categoria", 4);
            }
            database.insert(DbHelper.TABLE_CURSO, null, values);
        }
    }

    private static String generateRandomDescription(String courseName) {

        switch (courseName) {
            case "Curso de Java Avanzado":
                return "Conoce los conceptos clave del lenguaje de programación que se está comiendo al mundo. Aprende qué es una variable, una función, un objeto y dónde se guardan esos valores";
            case "Desarrollo de Aplicaciones Android":
                return "Sé parte de la comunidad que ya se está preparando para los desafíos de la IA.";
            case "Diseño de Interfaces de Usuario":
                return "El diseñador UX se centra en investigar y analizar las necesidades del cliente";
            case "Master Completo en Java de cero a experto":
                return "Aprende con el mejor curso Java de cero con las mejores prácticas POO, Java EE 9, CDI, JPA, EJB, JSF, Web Services, JAAS.";
            case "Introducción a la programación en Android":
                return "En esta formación intensiva se presentan los primeros pasos para el desarrollo de aplicaciones móviles a partir del lenguaje Java para Android, desde las versiones iniciales del sistema operativo hasta las versiones finales.";
            case "Curso de diseño de experiencia de usuario e interacciones":
                return "El diseño de experiencia de usuario es una práctica que sirve para diseñar productos centrados en las necesidades de los usuarios. Esta práctica está relacionada a su vez, con el diseño de interacciones como mediador del usuario con el proceso por medio de una interfaz.";
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

    private static void createUserAndAssignCourses(SQLiteDatabase database) {
        // Crear un nuevo usuario en la tabla Usuarios
        ContentValues usuarioValues = new ContentValues();
        usuarioValues.put("nombre", "Romeo");
        usuarioValues.put("apellido", "Moreno");
        usuarioValues.put("email", "romeo@gmail.com");

        // Encriptar la contraseña antes de guardarla directamente en DataGenerator
        String hashedPassword = DbUsuarios.hashPassword("romeo123"); // Llamada estática
        usuarioValues.put("password", hashedPassword);

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
            values.put("es_favorito", 0);
            database.insert(DbHelper.TABLE_INTER_CUR_USER, null, values);
        }
    }

}
