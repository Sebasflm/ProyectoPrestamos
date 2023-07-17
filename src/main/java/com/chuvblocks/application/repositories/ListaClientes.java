package com.chuvblocks.application.repositories;

import com.chuvblocks.application.entities.Cliente;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class ListaClientes {
    private final Set<Cliente> usuarios;

    public ListaClientes() {
        this.usuarios = new LinkedHashSet<>();
        this.usuarios.add(new Cliente("Juan", "Perez", "1234567890"));
    }

    public boolean agregarUsuario(Cliente usuario){
        return usuarios.add(usuario);
    }

    public void eliminarUsuario(Cliente usuario){
        usuarios.remove(usuario);
    }

    public Set<Cliente> obtenerUsuarios() {
        return usuarios;
    }

}
