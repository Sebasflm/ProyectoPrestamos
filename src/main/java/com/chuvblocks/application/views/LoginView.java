package com.chuvblocks.application.views;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class LoginView extends VerticalLayout {

    public LoginView() {
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(event -> {
            boolean isAuthenticated = authenticate(event.getUsername(), event.getPassword());
            if (isAuthenticated) {
                Notification.show("Inicio de sesión correcto!");
                getUI().ifPresent(ui -> ui.navigate("gestionUsuarios"));
            } else {
                loginForm.setError(true);
            }
        });

        add(loginForm);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHeightFull();
    }

    private boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("password");
    }
}
