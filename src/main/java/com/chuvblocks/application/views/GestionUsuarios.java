package com.chuvblocks.application.views;

import com.chuvblocks.application.entities.Usuario;
import com.chuvblocks.application.repositories.ListaUsuarios;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;

@Route(value = "gestionUsuarios")
@PageTitle("Gestión de Usuarios")
public class GestionUsuarios extends VerticalLayout {
    private ListaUsuarios listaUsuarios;
    private Grid<Usuario> grid;
    private Button addButton;

    public GestionUsuarios(ListaUsuarios listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @PostConstruct
    public void init() {
        grid = new Grid<>(Usuario.class);
        grid.setColumns("nombre", "edad", "cedula");
        grid.setItems(listaUsuarios.obtenerUsuarios());
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.asSingleSelect().addValueChangeListener(event -> {
            Usuario selectedUsuario = event.getValue();
            if (selectedUsuario != null) {
                abrirDialogoEditarUsuario(selectedUsuario);
            }
        });

        addButton = new Button("Agregar Usuario");
        addButton.addClickListener(event -> abrirDialogoAgregarUsuario());

        add(grid, addButton);
    }

    private void abrirDialogoAgregarUsuario() {
        Dialog dialog = new Dialog();
        TextField nombreField = new TextField("Nombre");
        TextField edadField = new TextField("Edad");
        TextField cedulaField = new TextField("Cédula");

        FormLayout formLayout = new FormLayout();
        formLayout.add(nombreField, edadField, cedulaField);

        Binder<Usuario> binder = new Binder<>(Usuario.class);
        binder.bind(nombreField, Usuario::getNombre, Usuario::setNombre);
        binder.forField(edadField)
                .withConverter(Integer::valueOf, String::valueOf)
                .bind(Usuario::getEdad, Usuario::setEdad);
        binder.forField(cedulaField)
                .withValidator(cedula -> cedula.length() >= 10, "La cédula debe tener al menos 10 caracteres")
                .bind(Usuario::getCedula, Usuario::setCedula);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Button addButton = new Button("Agregar", event -> {
            if (binder.validate().isOk()) {
                Usuario usuario = new Usuario(nombreField.getValue(), Integer.parseInt(edadField.getValue()), cedulaField.getValue());
                boolean added = listaUsuarios.agregarUsuario(usuario);
                if (added) {
                    grid.setItems(listaUsuarios.obtenerUsuarios());
                    dialog.close();
                }
            }
        });

        Div buttonsLayout = new Div(cancelButton, addButton);

        dialog.add(formLayout, buttonsLayout);
        dialog.open();
    }

    private void abrirDialogoEditarUsuario(Usuario usuario) {
        Dialog dialog = new Dialog();
        TextField nombreField = new TextField("Nombre", usuario.getNombre());
        TextField edadField = new TextField("Edad", String.valueOf(usuario.getEdad()));
        TextField cedulaField = new TextField("Cédula", usuario.getCedula());

        FormLayout formLayout = new FormLayout();
        formLayout.add(nombreField, edadField, cedulaField);

        Binder<Usuario> binder = new Binder<>(Usuario.class);
        binder.bind(nombreField, Usuario::getNombre, Usuario::setNombre);
        binder.forField(edadField)
                .withConverter(Integer::valueOf, String::valueOf)
                .bind(Usuario::getEdad, Usuario::setEdad);
        binder.forField(cedulaField)
                .withValidator(cedula -> cedula.length() >= 10, "La cédula debe tener al menos 10 caracteres")
                .bind(Usuario::getCedula, Usuario::setCedula);

        Button deleteButton = new Button("Eliminar", event -> {
            listaUsuarios.eliminarUsuario(usuario);
            grid.setItems(listaUsuarios.obtenerUsuarios());
            dialog.close();
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Div buttonsLayout = new Div(deleteButton, cancelButton);

        dialog.add(formLayout, buttonsLayout);
        dialog.open();
    }
}