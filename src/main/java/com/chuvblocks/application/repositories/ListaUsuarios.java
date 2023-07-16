package com.chuvblocks.application.repositories;

import com.chuvblocks.application.entities.Usuario;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class ListaUsuarios {
    private final Set<Usuario> usuarios;

    public ListaUsuarios() {
        this.usuarios = new LinkedHashSet<>();
        this.usuarios.add(new Usuario("Juan", 10, "123"));
    }

    public boolean agregarUsuario(Usuario usuario){
        return usuarios.add(usuario);
    }

    public Set<Usuario> obtenerUsuarios() {
        return usuarios;
    }

    public void eliminarUsuario(Usuario usuario){
        usuarios.remove(usuario);
    }
}
