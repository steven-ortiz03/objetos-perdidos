package objetosperdidos.model;

import java.time.LocalDate;

/**
 * Criterios de búsqueda del catálogo (RF-16, RF-17, RF-18).
 * Un criterio en null significa "no filtrar por ese dato".
 */
public class FiltroObjeto {

    private String texto;
    private Categoria categoria;
    private Lugar lugarEncontrado;
    private EstadoObjeto estado;
    private LocalDate desde;
    private LocalDate hasta;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

    public EstadoObjeto getEstado() {
        return estado;
    }

    public void setEstado(EstadoObjeto estado) {
        this.estado = estado;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
    }
}
