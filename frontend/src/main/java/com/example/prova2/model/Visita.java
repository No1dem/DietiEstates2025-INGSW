package com.example.prova2.model;

import java.sql.Date;
import java.sql.Time;

public class Visita {
    private Date dataVisita;

    private Time orarioVisita;

    private Cliente clientePrenotato;

    private Immobile immobileRichiestoVisita;

    private Agente agenteRiferimento;

    private boolean accettata;
}
