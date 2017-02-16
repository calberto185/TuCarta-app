package com.jassgroup.acceso.clases;

import java.io.Serializable;

/**
 * Created by kenwhiston on 13/06/2016.
 */
public class Usuario implements Serializable {
    public String apellidos;
    public String nombres;
    public String correo;
    public String clave;
    public String telefono;
    public String imagen;

    public Usuario(){

    }

    public Usuario(String apellidos, String nombres, String correo, String clave, String telefono, String imagen){
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.correo = correo;
        this.clave = clave;
        this.telefono = telefono;
        this.imagen = imagen;
    }

}
