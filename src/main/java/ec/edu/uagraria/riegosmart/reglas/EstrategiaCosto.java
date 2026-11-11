package ec.edu.uagraria.riegosmart.reglas;

import ec.edu.uagraria.riegosmart.dominio.Cultivo;
import ec.edu.uagraria.riegosmart.dominio.InsumoRiego;

import java.util.List;

/** STRATEGY (Taller 3): contrato de un calculo de costo de riego. */
@FunctionalInterface
public interface EstrategiaCosto {

    double calcular(Cultivo cultivo, List<InsumoRiego> insumos);
}
