package com.chuvblocks.application.repositories;

import com.chuvblocks.application.entities.Cliente;
import com.chuvblocks.application.entities.Credito;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class ListaCreditos {
    Set<Credito> listaCreditos;

    public ListaCreditos() {
        this.listaCreditos = new LinkedHashSet<>();
    }

    public Set<Credito> getListaCreditos() {
        return listaCreditos;
    }

    public boolean agregarCredito(Credito credito) {
        return listaCreditos.add(credito);
    }

    public boolean borrarCredito(Credito credito) {
        return listaCreditos.remove(credito);
    }

    public boolean borrarCreditoPorCliente(Cliente cliente) {
        return listaCreditos.removeIf(credito -> credito.getCliente().equals(cliente));
    }
}
