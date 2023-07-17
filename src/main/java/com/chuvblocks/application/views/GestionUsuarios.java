package com.chuvblocks.application.views;

import com.chuvblocks.application.entities.Cliente;
import com.chuvblocks.application.repositories.ListaClientes;
import com.chuvblocks.application.repositories.ListaCreditos;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;

@Route(value = "gestionUsuarios")
@PageTitle("Gestión de Usuarios")
public class GestionUsuarios extends HorizontalLayout {
    private final ListaClientes listaClientes;
    private final ListaCreditos listaCreditos;
    private Grid<Cliente> grid;

    public GestionUsuarios(ListaClientes listaUsuarios, ListaCreditos listaCreditos) {
        this.listaClientes = listaUsuarios;
        this.listaCreditos = listaCreditos;
    }

    @PostConstruct
    public void init() {
        SideNav nav = new SideNav();

        SideNavItem usuarios = new SideNavItem("Usuarios", GestionUsuarios.class, VaadinIcon.USER.create());
        SideNavItem creditos = new SideNavItem("Créditos", GestionCreditos.class, VaadinIcon.MONEY.create());
        SideNavItem amortizacion = new SideNavItem("Amortización", TablaAmortizacionView.class, VaadinIcon.TABLE.create());

        nav.addItem(usuarios, creditos,amortizacion);

        VerticalLayout content = new VerticalLayout();

        grid = new Grid<>(Cliente.class);
        grid.setColumns("nombre", "apellido", "cedula");
        grid.setItems(listaClientes.obtenerUsuarios());
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.asSingleSelect().addValueChangeListener(event -> {
            Cliente selectedUsuario = event.getValue();
            if (selectedUsuario != null) {
                abrirDialogoEditarUsuario(selectedUsuario);
            }
        });

        Button addButton = new Button("Agregar Cliente");
        addButton.addClickListener(event -> abrirDialogoAgregarUsuario());

        content.add(grid, addButton);

        setMargin(true);

        add(nav,content);
    }

    private void abrirDialogoAgregarUsuario() {
        Dialog dialog = new Dialog();
        TextField nombreField = new TextField("Nombre");
        TextField apellidoField = new TextField("Apellido");
        TextField cedulaField = new TextField("Cédula");

        FormLayout formLayout = new FormLayout();
        formLayout.add(nombreField, apellidoField, cedulaField);

        Binder<Cliente> binder = new Binder<>(Cliente.class);
        binder.bind(nombreField, Cliente::getNombre, Cliente::setNombre);
        binder.bind(apellidoField, Cliente::getApellido, Cliente::setApellido);
        binder.forField(cedulaField)
                .withValidator(cedula -> cedula.length() == 10, "La cédula debe tener al menos 10 caracteres")
                .bind(Cliente::getCedula, Cliente::setCedula);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Button addButton = new Button("Agregar", event -> {
            if (binder.validate().isOk()) {
                Cliente usuario = new Cliente(nombreField.getValue(), apellidoField.getValue(), cedulaField.getValue());
                boolean added = listaClientes.agregarUsuario(usuario);
                if (added) {
                    grid.setItems(listaClientes.obtenerUsuarios());
                    dialog.close();
                } else {
                    Notification.show("No se pudo agregar el usuario");
                }
            }
        });

        Div buttonsLayout = new Div(cancelButton, addButton);

        dialog.add(formLayout, buttonsLayout);
        dialog.open();
    }

    private void abrirDialogoEditarUsuario(Cliente usuario) {
        Dialog dialog = new Dialog();
        TextField nombreField = new TextField("Nombre");
        TextField apellidoField = new TextField("Apellido");
        TextField cedulaField = new TextField("Cédula");

        nombreField.setReadOnly(true);
        apellidoField.setReadOnly(true);
        cedulaField.setReadOnly(true);

        nombreField.setValue(usuario.getNombre());
        apellidoField.setValue(usuario.getApellido());
        cedulaField.setValue(usuario.getCedula());

        FormLayout formLayout = new FormLayout();
        formLayout.add(nombreField, apellidoField, cedulaField);

        Button deleteButton = new Button("Eliminar", event -> {
            listaClientes.eliminarUsuario(usuario);
            listaCreditos.borrarCreditoPorCliente(usuario);
            grid.setItems(listaClientes.obtenerUsuarios());
            dialog.close();
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Div buttonsLayout = new Div(deleteButton, cancelButton);

        dialog.add(formLayout, buttonsLayout);
        dialog.open();
    }
}