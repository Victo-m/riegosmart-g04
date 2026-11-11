package ec.edu.uagraria.riegosmart.dominio;

/** Todo producto del inventario tiene un codigo unico y legible. */
public interface Trazable {

    String codigo();

    /** Genera codigos del tipo CUL-0001, TUR-0002, INS-0003. */
    static String generarCodigo(String prefijo, int secuencia) {
        if (prefijo == null || prefijo.isBlank()) {
            throw new IllegalArgumentException("El prefijo es obligatorio");
        }
        if (secuencia <= 0) {
            throw new IllegalArgumentException("La secuencia debe ser positiva");
        }
        return String.format("%s-%04d", prefijo.toUpperCase(), secuencia);
    }
}
