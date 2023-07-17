package com.chuvblocks.application.views;

import com.chuvblocks.application.entities.Cliente;
import com.chuvblocks.application.entities.Credito;
import com.chuvblocks.application.entities.TablaAmortizacion;
import com.chuvblocks.application.repositories.ListaClientes;
import com.chuvblocks.application.repositories.ListaCreditos;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;

import java.util.List;

@Route("tablaAmortizacion")
@PageTitle("Tabla de Amortización")
public class TablaAmortizacionView extends HorizontalLayout {
    private final ListaCreditos listaCreditos;
    private final ListaClientes listaClientes;

    private Grid<TablaAmortizacion> grid;
    private Button btnGenerar;

    public TablaAmortizacionView(ListaCreditos listaCreditos, ListaClientes listaClientes) {
        this.listaCreditos = listaCreditos;
        this.listaClientes = listaClientes;
    }

    @PostConstruct
    public void inicializar() {
        SideNav nav = new SideNav();

        SideNavItem usuarios = new SideNavItem("Usuarios", GestionUsuarios.class, VaadinIcon.USER.create());
        SideNavItem creditos = new SideNavItem("Créditos", GestionCreditos.class, VaadinIcon.MONEY.create());
        SideNavItem amortizacion = new SideNavItem("Amortización", TablaAmortizacionView.class, VaadinIcon.TABLE.create());

        nav.addItem(usuarios, creditos,amortizacion);

        setMargin(true);

        add(nav, inicializarView());
    }

    public VerticalLayout inicializarView() {
        VerticalLayout content = new VerticalLayout();

        if (listaClientes.obtenerUsuarios().isEmpty()) {
            content.setAlignItems(Alignment.CENTER);
            Text text = new Text("No hay usuarios registrados");
            content.add(text);
            return content;
        }

        ComboBox<Cliente> listaClientesCbo = new ComboBox<>("Seleccione un cliente");
        listaClientesCbo.setItems(listaClientes.obtenerUsuarios());

        btnGenerar = new Button("Generar tabla de amortización");
        btnGenerar.setEnabled(false);

        HorizontalLayout seleccionClientes = new HorizontalLayout();
        seleccionClientes.setAlignItems(Alignment.END);
        seleccionClientes.add(listaClientesCbo, btnGenerar);

        grid = new Grid<>();
        grid.addColumn(TablaAmortizacion::getNumeroCuota).setHeader("Cuota");
        grid.addColumn(TablaAmortizacion::getCapitalAmortizadoAsString).setHeader("Capital Amortizado");
        grid.addColumn(TablaAmortizacion::getInteresAsString).setHeader("Interés");
        grid.addColumn(TablaAmortizacion::getSaldoPendienteAsString).setHeader("Saldo Pendiente");
        grid.setWidth("100%");

        listaClientesCbo.addValueChangeListener(event -> {
            Cliente selectedCliente = event.getValue();
            btnGenerar.setEnabled(selectedCliente != null);
        });

        btnGenerar.addClickListener(event -> {
            Cliente selectedCliente = listaClientesCbo.getValue();
            if (selectedCliente != null) {
                Credito selectedCredito = listaCreditos.buscarCreditoPorCliente(selectedCliente);
                if (selectedCredito != null) {
                    List<TablaAmortizacion> tablaAmortizacion = TablaAmortizacion.generarTablaAmortizacion(selectedCredito);
                    grid.setItems(tablaAmortizacion);
                } else {
                    Notification.show("No hay créditos para este cliente");
                }
            }
        });

        content.add(seleccionClientes, grid);
        return content;
    }
}
