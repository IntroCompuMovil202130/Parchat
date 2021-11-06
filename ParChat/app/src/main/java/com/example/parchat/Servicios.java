package com.example.parchat;

import java.util.ArrayList;
import java.util.List;

public class Servicios {

    public List<Usuario> usuarios;

    public Servicios(){
        this.usuarios = new ArrayList<>();
        Usuario u1 = new Usuario();
        u1.foto = String.valueOf(R.drawable.user1);
        u1.nombre = "Fabian Rojas";
        u1.id = "1";
        this.usuarios.add(u1);
    }

    public Usuario buscarUsuario(String id){
        for( Usuario u: this.usuarios ){
            if(u.id.equals(id))
                return u;
        }return null;
    }

}
