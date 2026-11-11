package ec.edu.uagraria.riegosmart.alertas;

/** OBSERVER: quien quiera enterarse de las alertas implementa este metodo. */
@FunctionalInterface
public interface ObservadorAlertas {

    void notificar(Alerta alerta);
}
