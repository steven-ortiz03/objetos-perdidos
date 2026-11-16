package objetosperdidos.model;

import java.time.LocalDate;

/** Registro de la devolución de un objeto a su propietario. */
public class Entrega {

    private int id;
    private int idObjeto;
    private String nombreObjeto;       // se llena al consultar el historial
    private String nombreReceptor;
    private String documentoReceptor;
    private LocalDate fechaEntrega;
    private String observaciones;
    private int idFuncionario;
    private String nombreFuncionario;  // se llena al consultar el historial

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(int idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public void setNombreReceptor(String nombreReceptor) {
        this.nombreReceptor = nombreReceptor;
    }

    public String getDocumentoReceptor() {
        return documentoReceptor;
    }

    public void setDocumentoReceptor(String documentoReceptor) {
        this.documentoReceptor = documentoReceptor;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNombreFuncionario() {
        return nombreFuncionario;
    }

    public void setNombreFuncionario(String nombreFuncionario) {
        this.nombreFuncionario = nombreFuncionario;
    }
}
