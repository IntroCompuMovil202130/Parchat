package com.example.parchat;

import java.util.ArrayList;
import java.util.List;

public class Servicios {

    public List<Usuario> usuarios;

    public Servicios(){
        this.usuarios = new ArrayList<>();
        //this.usuarios.add( new Usuario(1, "Mia Khalifa", R.drawable.mia_khalifa, new ArrayList<Integer>()) );
        //this.usuarios.add( new Usuario(2, "Lana Rhoades", R.drawable.lanita, new ArrayList<Integer>()) );
    }

    public Usuario buscarUsuario(int id){
        for( Usuario u: this.usuarios ){
            if( u.getId() == id )
                return u;
        }return null;
    }

}
