package ec.edu.uagraria.riegosmart.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Guarda los productos del sistema. Solo almacena y consulta: no calcula ni alerta. */
public class Inventario {

    private final List<ProductoRiego> productos = new ArrayList<>();

    public void agregar(ProductoRiego producto) {
        if (producto == null) {
            throw new IllegalArgumentException("No se puede agregar un producto nulo");
        }
        productos.add(producto);
    }

    public Optional<ProductoRiego> buscar(String codigo) {
        return productos.stream().filter(p -> p.codigo().equals(codigo)).findFirst();
    }

    public List<ProductoRiego> todos() {
        return List.copyOf(productos);
    }

    public List<Cultivo> cultivos() {
        return productos.stream()
                .filter(p -> p instanceof Cultivo)
                .map(p -> (Cultivo) p)
                .toList();
    }

    public List<InsumoRiego> insumos() {
        return productos.stream()
                .filter(p -> p instanceof InsumoRiego)
                .map(p -> (InsumoRiego) p)
                .toList();
    }

    /** Etiqueta del tipo con switch de patrones: sin default porque la jerarquia es sellada. */
    public static String tipoDe(ProductoRiego producto) {
        return switch (producto) {
            case Cultivo c -> "Cultivo";
            case TurnoRiego t -> "Turno de riego";
            case InsumoRiego i -> "Insumo de riego";
        };
    }

    public int tamanio() {
        return productos.size();
    }
}
