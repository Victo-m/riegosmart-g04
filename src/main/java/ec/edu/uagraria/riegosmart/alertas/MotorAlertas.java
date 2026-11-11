package ec.edu.uagraria.riegosmart.alertas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Sujeto del OBSERVER: compone reglas y observadores en lugar de heredar. */
public class MotorAlertas {

    private final List<ReglaAlerta> reglas = new ArrayList<>();
    private final List<ObservadorAlertas> observadores = new ArrayList<>();

    public void agregarRegla(ReglaAlerta regla) {
        reglas.add(regla);
    }

    public void suscribir(ObservadorAlertas observador) {
        observadores.add(observador);
    }

    /** Evalua todas las reglas y devuelve cuantas alertas se publicaron. */
    public int procesar(LecturaParcela lectura) {
        int publicadas = 0;
        for (ReglaAlerta regla : reglas) {
            Optional<Alerta> alerta = regla.evaluar(lectura);
            if (alerta.isPresent()) {
                observadores.forEach(o -> o.notificar(alerta.get()));
                publicadas++;
            }
        }
        return publicadas;
    }
}
