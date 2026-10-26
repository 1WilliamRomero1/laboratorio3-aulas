package edu.ufg.aulas.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "aulas",
        uniqueConstraints = @UniqueConstraint(name = "uk_aulas_nombre_edificio",
                columnNames = {"nombre", "edificio"}))
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "edificio", nullable = false, length = 80)
    private String edificio;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "planta_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_aulas_planta"))
    private Planta planta;

    @Column(name = "capacidad", nullable = false)
    private int capacidad;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tipo_aula_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_aulas_tipo_aula"))
    private TipoAula tipoAula;

    @Column(name = "responsable", nullable = false, length = 100)
    private String responsable;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "estado_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_aulas_estado"))
    private EstadoAula estado;

    public Aula() {
    }

    public Aula(String nombre, String edificio, Planta planta, int capacidad,
            TipoAula tipoAula, String responsable, EstadoAula estado) {
        this.nombre = nombre;
        this.edificio = edificio;
        this.planta = planta;
        this.capacidad = capacidad;
        this.tipoAula = tipoAula;
        this.responsable = responsable;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public TipoAula getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(TipoAula tipoAula) {
        this.tipoAula = tipoAula;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public EstadoAula getEstado() {
        return estado;
    }

    public void setEstado(EstadoAula estado) {
        this.estado = estado;
    }
}
