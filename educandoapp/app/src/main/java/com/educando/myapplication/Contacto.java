package com.educando.myapplication;

public class Contacto {
    private int id;
    private String fecha;
    private int idUsuario;
    private String titulo;
    private String mensaje;

    public Contacto(int id, String fecha, int idUsuario, String titulo, String mensaje) {
        this.id = id;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }
}
