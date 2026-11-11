package ec.edu.uagraria.riegosmart.fabrica;

/** Los tres tipos que sabe crear la fabrica, con el prefijo de su codigo. */
public enum TipoProducto {
    CULTIVO("CUL"),
    TURNO_RIEGO("TUR"),
    INSUMO_RIEGO("INS");

    private final String prefijo;

    TipoProducto(String prefijo) {
        this.prefijo = prefijo;
    }

    public String prefijo() {
        return prefijo;
    }
}
