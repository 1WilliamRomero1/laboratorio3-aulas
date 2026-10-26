package edu.ufg.aulas;

import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.util.DataInitializer;
import edu.ufg.aulas.util.HibernateUtil;
import edu.ufg.aulas.util.Mensajes;
import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage escenarioPrincipal;
    private static Usuario usuarioActual;

    @Override
    public void start(Stage stage) {
        escenarioPrincipal = stage;
        try {
            DataInitializer.inicializar();
        } catch (Exception e) {
            Mensajes.error("No se pudo conectar con la base de datos.\n\n"
                    + "Verifique que MySQL esté encendido y que el usuario y la clave "
                    + "de hibernate.cfg.xml sean correctos.\n\nDetalle: " + e.getMessage());
            Platform.exit();
            return;
        }
        cambiarEscena("Login", "Sistema de Aulas - Iniciar sesión");
        stage.show();
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void cambiarEscena(String vista, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/vistas/" + vista + ".fxml"));
            Parent raiz = loader.load();
            Scene escena = new Scene(raiz);
            escena.getStylesheets().add(App.class.getResource("/estilos/estilos.css").toExternalForm());
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.setTitle(titulo);
            escenarioPrincipal.sizeToScene();
            escenarioPrincipal.centerOnScreen();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar la vista " + vista, e);
        }
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
