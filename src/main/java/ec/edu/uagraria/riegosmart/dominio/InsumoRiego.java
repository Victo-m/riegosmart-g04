package ec.edu.uagraria.riegosmart.dominio;

/**
 * Insumo usado en el sistema de riego (cinta de goteo, fertilizante soluble, etc.).
 */
public record InsumoRiego(String codigo, String nombre, String unidad,
                          double cantidad, double precioUnitario) implements ProductoRiego {

    public InsumoRiego {
        if (cantidad < 0 || precioUnitario < 0) {
            throw new IllegalArgumentException(
                    "Cantidad y precio deben ser mayores o iguales a cero");
        }
    }

    /** Costo total = cantidad x precio unitario. */
    public double costo() {
        return cantidad * precioUnitario;
    }

    @Override
    public String descripcion() {
        return nombre + " (" + cantidad + " " + unidad + ")";
    }
}
