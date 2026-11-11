package ec.edu.uagraria.riegosmart.reglas;

import ec.edu.uagraria.riegosmart.dominio.Cultivo;
import ec.edu.uagraria.riegosmart.dominio.InsumoRiego;

import java.util.List;
import java.util.Optional;

/** Contexto del Strategy: usa una estrategia sin saber como calcula. */
public class CalculadoraCostos {

    private EstrategiaCosto estrategia;

    public CalculadoraCostos() { }

    public CalculadoraCostos(EstrategiaCosto estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaCosto estrategia) {
        this.estrategia = estrategia;
    }

    public Optional<Double> calcular(Cultivo cultivo, List<InsumoRiego> insumos) {
        if (estrategia == null || cultivo == null || insumos == null) {
            return Optional.empty();
        }
        return Optional.of(estrategia.calcular(cultivo, insumos));
    }

    public static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
