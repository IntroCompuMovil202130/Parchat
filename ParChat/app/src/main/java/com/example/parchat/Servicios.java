package com.example.parchat;

import java.util.ArrayList;
import java.util.List;

public class Servicios {

    public List<Usuario> usuarios;

    public Servicios(){
        this.usuarios = new ArrayList<>();
        this.usuarios.add( new Usuario("1", "Julian Tarazona", "", new ArrayList<String>()) );
        this.usuarios.add( new Usuario("2", "Fabian Rojas", "", new ArrayList<String>()) );
    }

    public Usuario buscarUsuario(String id){
        for( Usuario u: this.usuarios ){
            if(u.getId().equals(id))
                return u;
        }return null;
    }

}
