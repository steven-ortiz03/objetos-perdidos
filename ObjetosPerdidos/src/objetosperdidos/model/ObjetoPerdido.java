package objetosperdidos.model;

import java.time.LocalDate;

/** Objeto encontrado en la institución y registrado en el sistema. */
public class ObjetoPerdido {

    private int id;
    private String nombre;
    private String descripcion;
    private String rutaFoto;
    private LocalDate fechaEncontrado;
    private EstadoObjeto estado = EstadoObjeto.DISPONIBLE; // RF-07: estado inicial
    private Categoria categoria;
    private Lugar lugarEncontrado;
    private Lugar lugarResguardo;
    private int idRegistradoPor;
    private LocalDate fechaEntrega; // solo tiene valor cuando el objeto fue entregado

    public boolean estaDisponible() {
        return estado == EstadoObjeto.DISPONIBLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    public LocalDate getFechaEncontrado() {
        return fechaEncontrado;
    }

    public void setFechaEncontrado(LocalDate fechaEncontrado) {
        this.fechaEncontrado = fechaEncontrado;
    }

    public EstadoObjeto getEstado() {
        return estado;
    }

    public void setEstado(EstadoObjeto estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Lugar getLugarEncontrado() {
        return lugarEncontrado;
    }

    public void setLugarEncontrado(Lugar lugarEncontrado) {
        this.lugarEncontrado = lugarEncontrado;
    }

    public Lugar getLugarResguardo() {
        return lugarResguardo;
    }

    public void setLugarResguardo(Lugar lugarResguardo) {
        this.lugarResguardo = lugarResguardo;
    }

    public int getIdRegistradoPor() {
        return idRegistradoPor;
    }

    public void setIdRegistradoPor(int idRegistradoPor) {
        this.idRegistradoPor = idRegistradoPor;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}
