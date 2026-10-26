package edu.ufg.aulas.controladores;

import edu.ufg.aulas.App;
import edu.ufg.aulas.dao.UsuarioDAO;
import edu.ufg.aulas.entidades.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtClave;
    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void ingresar() {
        String correo = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();
        String clave = txtClave.getText() == null ? "" : txtClave.getText();

        if (correo.isEmpty() || clave.isEmpty()) {
            mostrarError("Ingrese su correo y su clave.");
            return;
        }
        if (!correo.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            mostrarError("El correo no tiene un formato válido.");
            return;
        }

        Usuario usuario;
        try {
            usuario = usuarioDAO.autenticar(correo, clave);
        } catch (Exception e) {
            mostrarError("No se pudo consultar la base de datos: " + e.getMessage());
            return;
        }

        if (usuario == null) {
            mostrarError("Correo o clave incorrectos.");
            txtClave.clear();
            return;
        }

        App.setUsuarioActual(usuario);
        App.cambiarEscena("Home", "Sistema de Aulas - Registro de aulas");
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
    }
}
