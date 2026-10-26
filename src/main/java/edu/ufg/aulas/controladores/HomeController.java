package edu.ufg.aulas.controladores;

import edu.ufg.aulas.App;
import edu.ufg.aulas.dao.AulaDAO;
import edu.ufg.aulas.dao.CatalogoDAO;
import edu.ufg.aulas.entidades.Aula;
import edu.ufg.aulas.entidades.EstadoAula;
import edu.ufg.aulas.entidades.Planta;
import edu.ufg.aulas.entidades.TipoAula;
import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.util.Mensajes;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class HomeController {

    @FXML
    private Label lblUsuario;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtEdificio;
    @FXML
    private ComboBox<Planta> cmbPlanta;
    @FXML
    private TextField txtCapacidad;
    @FXML
    private ComboBox<TipoAula> cmbTipoAula;
    @FXML
    private TextField txtResponsable;
    @FXML
    private ComboBox<EstadoAula> cmbEstado;

    @FXML
    private TableView<Aula> tablaAulas;
    @FXML
    private TableColumn<Aula, String> colNombre;
    @FXML
    private TableColumn<Aula, String> colEdificio;
    @FXML
    private TableColumn<Aula, String> colPlanta;
    @FXML
    private TableColumn<Aula, String> colCapacidad;
    @FXML
    private TableColumn<Aula, String> colTipo;
    @FXML
    private TableColumn<Aula, String> colResponsable;
    @FXML
    private TableColumn<Aula, String> colEstado;

    private final CatalogoDAO catalogoDAO = new CatalogoDAO();
    private final AulaDAO aulaDAO = new AulaDAO();

    @FXML
    private void initialize() {
        Usuario usuario = App.getUsuarioActual();
        lblUsuario.setText(usuario == null ? ""
                : "Sesión: " + usuario.getNombre() + " (" + usuario.getCorreo() + ")");

        configurarCombo(cmbPlanta);
        configurarCombo(cmbTipoAula);
        configurarCombo(cmbEstado);
        configurarTabla();
        cargarCatalogos();
        cargarTabla();
    }

    private <T> void configurarCombo(ComboBox<T> combo) {
        combo.setButtonCell(new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? combo.getPromptText() : item.toString());
            }
        });
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colEdificio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEdificio()));
        colPlanta.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getPlanta().getNumero())));
        colCapacidad.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getCapacidad())));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoAula().getNombre()));
        colResponsable.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getResponsable()));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().getNombre()));
    }

    private void cargarCatalogos() {
        try {
            cmbPlanta.setItems(FXCollections.observableArrayList(catalogoDAO.listarPlantas()));
            cmbTipoAula.setItems(FXCollections.observableArrayList(catalogoDAO.listarTiposAula()));
            cmbEstado.setItems(FXCollections.observableArrayList(catalogoDAO.listarEstados()));
        } catch (Exception e) {
            Mensajes.error("No se pudieron cargar los catálogos desde la base de datos:\n" + e.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            tablaAulas.setItems(FXCollections.observableArrayList(aulaDAO.listar()));
        } catch (Exception e) {
            Mensajes.error("No se pudo cargar la lista de aulas:\n" + e.getMessage());
        }
    }

    @FXML
    private void guardar() {
        String nombre = texto(txtNombre);
        String edificio = texto(txtEdificio);
        String responsable = texto(txtResponsable);
        String capacidadTexto = texto(txtCapacidad);
        Planta planta = cmbPlanta.getValue();
        TipoAula tipoAula = cmbTipoAula.getValue();
        EstadoAula estado = cmbEstado.getValue();

        List<String> errores = new ArrayList<>();

        if (nombre.isEmpty()) {
            errores.add("El nombre del aula es obligatorio.");
        } else if (nombre.length() > 50) {
            errores.add("El nombre del aula no puede superar 50 caracteres.");
        }
        if (edificio.isEmpty()) {
            errores.add("El edificio es obligatorio.");
        } else if (edificio.length() > 80) {
            errores.add("El edificio no puede superar 80 caracteres.");
        }
        if (planta == null) {
            errores.add("Seleccione la planta.");
        } else if (planta.getNumero() <= 0) {
            errores.add("La planta debe ser un entero mayor que 0.");
        }

        int capacidad = 0;
        if (capacidadTexto.isEmpty()) {
            errores.add("La capacidad es obligatoria.");
        } else {
            try {
                capacidad = Integer.parseInt(capacidadTexto);
                if (capacidad <= 0) {
                    errores.add("La capacidad debe ser un entero mayor que 0.");
                }
            } catch (NumberFormatException e) {
                errores.add("La capacidad debe ser un número entero (sin letras ni decimales).");
            }
        }

        if (tipoAula == null) {
            errores.add("Seleccione el tipo de aula.");
        }
        if (responsable.isEmpty()) {
            errores.add("El responsable es obligatorio.");
        } else if (responsable.length() > 100) {
            errores.add("El responsable no puede superar 100 caracteres.");
        }
        if (estado == null) {
            errores.add("Seleccione el estado del aula.");
        }

        if (!errores.isEmpty()) {
            Mensajes.advertencia(String.join("\n", errores));
            return;
        }

        try {
            if (aulaDAO.existe(nombre, edificio)) {
                Mensajes.advertencia("Ya existe un aula llamada \"" + nombre
                        + "\" en el edificio \"" + edificio + "\".");
                return;
            }
            Aula aula = new Aula(nombre, edificio, planta, capacidad, tipoAula, responsable, estado);
            aulaDAO.guardar(aula);
            Mensajes.info("El aula \"" + nombre + "\" se guardó correctamente.");
            limpiar();
            cargarTabla();
        } catch (Exception e) {
            Mensajes.error("No se pudo guardar el aula:\n" + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtNombre.clear();
        txtEdificio.clear();
        txtCapacidad.clear();
        txtResponsable.clear();
        cmbPlanta.getSelectionModel().clearSelection();
        cmbTipoAula.getSelectionModel().clearSelection();
        cmbEstado.getSelectionModel().clearSelection();
        txtNombre.requestFocus();
    }

    @FXML
    private void cerrarSesion() {
        App.setUsuarioActual(null);
        App.cambiarEscena("Login", "Sistema de Aulas - Iniciar sesión");
    }

    private String texto(TextField campo) {
        String valor = campo.getText();
        return valor == null ? "" : valor.trim();
    }
}
