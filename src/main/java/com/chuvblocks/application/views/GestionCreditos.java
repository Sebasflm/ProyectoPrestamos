package com.chuvblocks.application.views;

import com.chuvblocks.application.entities.Cliente;
import com.chuvblocks.application.entities.Credito;
import com.chuvblocks.application.repositories.ListaClientes;
import com.chuvblocks.application.repositories.ListaCreditos;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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

import java.util.Objects;

@Route("gestionCreditos")
@PageTitle("Gestión de Créditos")
public class GestionCreditos extends HorizontalLayout {

    private final ListaCreditos listaCreditos;
    private final ListaClientes listaClientes;
    Grid<Credito> grid;

    public GestionCreditos(ListaCreditos listaCreditos, ListaClientes listaClientes) {
        this.listaCreditos = listaCreditos;
        this.listaClientes = listaClientes;
    }

    @PostConstruct
    public void inicializar() {
        SideNav nav = new SideNav();

        SideNavItem usuarios = new SideNavItem("Usuarios", GestionUsuarios.class, VaadinIcon.USER.create());
        SideNavItem creditos = new SideNavItem("Créditos", GestionCreditos.class, VaadinIcon.MONEY.create());

        nav.addItem(usuarios, creditos);

        VerticalLayout content = new VerticalLayout();

        grid = new Grid<>(Credito.class);
        grid.setColumns("cliente", "prestamo", "tasaInteres", "plazo", "valorCuotas");
        grid.setItems(listaCreditos.getListaCreditos());
        grid.asSingleSelect().addValueChangeListener(event -> {
            Credito selectedCredito = event.getValue();
            if (selectedCredito != null) {
                abrirDialogoEditarCredito(selectedCredito);
            }
        });

        Button addButton = new Button("Agregar Crédito");
        addButton.addClickListener(event -> abrirDialogoAgregarCredito());

        content.add(grid, addButton);

        setMargin(true);

        add(nav, content);
    }

    private void abrirDialogoAgregarCredito() {
        if (listaClientes.obtenerUsuarios().isEmpty()) {
            Notification.show("No hay clientes registrados");
            return;
        }
        Dialog dialog = new Dialog();
        ComboBox<Cliente> clientescbo = new ComboBox<>();
        TextField prestamoField = new TextField("Prestamo");
        TextField tasaInteresField = new TextField("Tasa Interes");
        TextField plazoField = new TextField("Plazo");

        clientescbo.setItems(listaClientes.obtenerUsuarios());
        clientescbo.setValue(listaClientes.obtenerUsuarios().stream().findFirst().get());

        FormLayout formLayout = new FormLayout();
        formLayout.add(clientescbo, prestamoField, tasaInteresField, plazoField);

        Binder<Credito> binder = new Binder<>(Credito.class);
        binder.bind(clientescbo, Credito::getCliente, Credito::setCliente);
        binder.forField(clientescbo).withValidator(Objects::nonNull, "Debe elegir un cliente");
        binder.forField(prestamoField).withConverter(Double::valueOf, String::valueOf);
        binder.forField(tasaInteresField).withConverter(Double::valueOf, String::valueOf);
        binder.forField(plazoField).withConverter(Integer::valueOf, String::valueOf);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Button addButton = new Button("Agregar", event -> {
            if (binder.validate().isOk()) {
                Credito nuevoCredito = new Credito(clientescbo.getValue(), Double.parseDouble(prestamoField.getValue()),
                        Double.parseDouble(tasaInteresField.getValue()), Integer.parseInt(plazoField.getValue()));
                boolean added = listaCreditos.agregarCredito(nuevoCredito);
                if (added) {
                    grid.setItems(listaCreditos.getListaCreditos());
                    dialog.close();
                } else {
                    Notification.show("No se pudo agregar el credito");
                }
            }
        });

        Div buttonsLayout = new Div(cancelButton, addButton);

        dialog.add(formLayout, buttonsLayout);
        dialog.open();
    }

    private void abrirDialogoEditarCredito(Credito credito) {
        Dialog dialog = new Dialog();
        TextField clienteField = new TextField("Cliente");
        TextField prestamoField = new TextField("Prestamo");
        TextField tasaInteresField = new TextField("Tasa Interes");
        TextField plazoField = new TextField("Plazo");

        clienteField.setReadOnly(true);
        clienteField.setValue(credito.getCliente().toString());
        prestamoField.setValue(String.valueOf(credito.getPrestamo()));
        tasaInteresField.setValue(String.valueOf(credito.getTasaInteres()));
        plazoField.setValue(String.valueOf(credito.getPlazo()));

        FormLayout formLayout = new FormLayout();
        formLayout.add(clienteField, prestamoField, tasaInteresField, plazoField);

        Button deleteButton = new Button("Eliminar", event -> {
            listaCreditos.borrarCredito(credito);
            grid.setItems(listaCreditos.getListaCreditos());
            dialog.close();
        });

        Button saveButton = new Button("Guardar", event -> {
            credito.setPrestamo(Double.parseDouble(prestamoField.getValue()));
            credito.setTasaInteres(Double.parseDouble(tasaInteresField.getValue()));
            credito.setPlazo(Integer.parseInt(plazoField.getValue()));
            credito.calcularValorCuotas();
            grid.setItems(listaCreditos.getListaCreditos());
            dialog.close();
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Div buttonsLayout = new Div(cancelButton, saveButton, deleteButton);

        dialog.add(formLayout, buttonsLayout);
        dialog.open();
    }
}