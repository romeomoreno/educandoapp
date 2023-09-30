package com.educando.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.educando.myapplication.Usuario;

public class DbUsuarios extends DbHelper {

    Context context;

    public DbUsuarios(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarContacto(String nombre, String apellido, String email, String password) {
        long id = 0;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("apellido", apellido);
            values.put("email", email);
            values.put("password", password);

            id = db.insert(TABLE_USUARIOS, null, values);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public boolean buscarUsuario(String email, String password) {
        boolean existe = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE email = ? AND password = ?";
            cursor = db.rawQuery(query, new String[]{email, password});

            if (cursor != null && cursor.getCount() > 0) {
                existe = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return existe;
    }

    public boolean actualizarEstadoLogueado(String email, int logueado) {
        boolean actualizado = false;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("logueado", logueado);

            int rowsAffected = db.update(TABLE_USUARIOS, values, "email = ?", new String[]{email});

            if (rowsAffected > 0) {
                actualizado = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return actualizado;
    }

    public Usuario obtenerUsuarioLogueado() {
        Usuario usuario = null;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE logueado = 1"; // Supongo que tienes una columna "logueado" para identificar al usuario logueado
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Obtén los datos del usuario logueado
                int idIndex = cursor.getColumnIndex("id_usuario");
                int nombreIndex = cursor.getColumnIndex("nombre");
                int apellidoIndex = cursor.getColumnIndex("apellido");
                int emailIndex = cursor.getColumnIndex("email");
                int passwordIndex = cursor.getColumnIndex("password");

                int id = cursor.getInt(idIndex);
                String nombre = cursor.getString(nombreIndex);
                String apellido = cursor.getString(apellidoIndex);
                String email = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);

                // Crea un objeto Usuario con los datos obtenidos
                usuario = new Usuario(id, nombre, apellido, email, password);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return usuario;
    }

    public boolean actualizarPassword(int idUsuario, String newPassword) {
        boolean actualizado = false;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("password", newPassword);

            int rowsAffected = db.update(TABLE_USUARIOS, values, "id_usuario = ?", new String[]{String.valueOf(idUsuario)});

            if (rowsAffected > 0) {
                actualizado = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return actualizado;
    }
}