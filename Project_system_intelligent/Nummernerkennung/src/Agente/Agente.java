package Agente;

import Tecnicas_IA.AlgoritmosGeneticos.LCS;
import java.util.BitSet;

public class Agente {
   
   LCS lcs;
    
    public Agente()
    {
        lcs = new LCS();
    }
    
    public void iniciar()
    {
        lcs.iniciarAprendizaje();
        lcs.iniciarEtapaValidacion();
    }
}
