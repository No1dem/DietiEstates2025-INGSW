package it.unina.dietiEstates25.model;

import jakarta.json.bind.JsonbException;
import jakarta.json.bind.adapter.JsonbAdapter;

public class ClasseEnergeticaAdapter implements JsonbAdapter<ClasseEnergetica, String> {

    @Override
    public ClasseEnergetica adaptFromJson(String json) {
        if (json == null) {
            return null;
        }
        try {
            // Prima prova con il valore mappato
            return ClasseEnergetica.fromString(json);
        } catch (IllegalArgumentException e1) {
            try {
                // Se fallisce, prova con il nome dell'enum
                return ClasseEnergetica.valueOf(json);
            } catch (IllegalArgumentException e2) {
                throw new JsonbException("Valore non valido per ClasseEnergetica: " + json);
            }
        }
    }
    @Override
    public String adaptToJson(ClasseEnergetica classe) {
        if (classe == null) {
            return null;
        }
        return classe.getValore(); // Usa il valore corretto, come "A++"
    }




}
