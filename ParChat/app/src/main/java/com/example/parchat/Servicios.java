package com.example.parchat;

import java.util.ArrayList;
import java.util.List;

public class Servicios {

    public List<Usuario> usuarios;

    public Servicios(){
        this.usuarios = new ArrayList<>();
        Usuario u1 = new Usuario();
        u1.setFoto(String.valueOf(R.drawable.user1));
        u1.setNombre("Fabian Rojas");
        u1.setId("1");
        this.usuarios.add(u1);

        Usuario u2 = new Usuario();
        u2.setFoto(String.valueOf(R.drawable.user2));
        u2.setNombre("Julian Trazona");
        u2.setId("2");
        this.usuarios.add( u2 );
    }

    public Usuario buscarUsuario(String id){
        for( Usuario u: this.usuarios ){
            if(u.getId().equals(id))
                return u;
        }return null;
    }

}
