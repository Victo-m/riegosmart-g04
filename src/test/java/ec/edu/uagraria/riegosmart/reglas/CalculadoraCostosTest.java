package ec.edu.uagraria.riegosmart.reglas;

import ec.edu.uagraria.riegosmart.dominio.Cultivo;
import ec.edu.uagraria.riegosmart.dominio.Especie;
import ec.edu.uagraria.riegosmart.dominio.InsumoRiego;
import ec.edu.uagraria.riegosmart.fabrica.DatosProducto;
import ec.edu.uagraria.riegosmart.fabrica.FabricaProductos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Las estrategias del Taller 3 siguen funcionando despues de mover los paquetes. */
class CalculadoraCostosTest {

    private Cultivo banano;
    private List<InsumoRiego> insumos;

    @BeforeEach
    void setUp() {
        FabricaProductos fabrica = new FabricaProductos();
        banano = fabrica.crearCultivo(DatosProducto.cultivo("P-01", Especie.BANANO, 4.0, 1200.0));
        insumos = List.of(
                fabrica.crearInsumo(DatosProducto.insumo("Cinta de goteo", "rollo", 6, 85.00)),
                fabrica.crearInsumo(DatosProducto.insumo("Fertilizante soluble", "saco", 10, 42.50)));
    }

    @Test
    @DisplayName("Costo directo suma los insumos")
    void costoDirecto() {
        var calc = new CalculadoraCostos(Estrategias.costoDirecto());
        assertEquals(935.00, calc.calcular(banano, insumos).orElseThrow(), 0.01);
    }

    @Test
    @DisplayName("Costo por hectarea divide para la superficie")
    void costoPorHectarea() {
        var calc = new CalculadoraCostos(Estrategias.costoPorHectarea());
        assertEquals(233.75, calc.calcular(banano, insumos).orElseThrow(), 0.01);
    }

    @Test
    @DisplayName("Con IVA aplica el 15 % recibido por parametro")
    void conIva() {
        var calc = new CalculadoraCostos(Estrategias.conIva(Estrategias.costoDirecto(), ParametrosCosto.ECUADOR));
        assertEquals(1075.25, calc.calcular(banano, insumos).orElseThrow(), 0.01);
    }

    @Test
    @DisplayName("Sin estrategia devuelve Optional vacio")
    void sinEstrategia() {
        assertTrue(new CalculadoraCostos().calcular(banano, insumos).isEmpty());
    }
}
