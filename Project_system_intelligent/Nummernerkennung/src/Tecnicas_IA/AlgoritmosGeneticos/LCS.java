package Tecnicas_IA.AlgoritmosGeneticos;

import Utilities.Utilidades;
import Utilities.XSRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;
import java.util.List;
import java.util.Random;

/*
 * @author Juan
 */
public class LCS {

    private int numeroReglas;
    private int parametroAprendizaje;
    private int longitudCromosoma;
    private int logitudFitness;
    private List<Regla> reglas;

    public int getNumeroReglas() {
        return numeroReglas;
    }

    public void setNumeroReglas(int numeroReglas) {
        this.numeroReglas = numeroReglas;
    }

    public int getParametroAprendizaje() {
        return parametroAprendizaje;
    }

    public void setParametroAprendizaje(int parametroAprendizaje) {
        this.parametroAprendizaje = parametroAprendizaje;
    }

    public int getLongitudCromosoma() {
        return longitudCromosoma;
    }

    public void setLongitudCromosoma(int longitudCromosoma) {
        this.longitudCromosoma = longitudCromosoma;
    }

    public int getLogitudFitness() {
        return logitudFitness;
    }

    public void setLogitudFitness(int logitudFitness) {
        this.logitudFitness = logitudFitness;
    }

    public void inicializarReglasAleatoriamente() {

    }

    public static Regla[] recombinarReglas(Regla r1, Regla r2, int longitudRegla) {
        int idx = Utilidades.intAleatorio(1, longitudRegla - 1);
        BitSet bs1 = new BitSet();
        BitSet bs2 = new BitSet();

        for (int i = 0; i < idx; i++) {
            if (r1.getCondicion().get(i)) {
                bs1.set(i);
            }
            if (r2.getCondicion().get(i)) {
                bs2.set(i);
            }
        }
        for (int i = idx; i < longitudRegla; i++) {
            if (r2.getCondicion().get(i)) {
                bs1.set(i);
            }
            if (r1.getCondicion().get(i)) {
                bs2.set(i);
            }
        }

        List<Integer> c1 = new ArrayList<Integer>();
        List<Integer> c2 = new ArrayList<Integer>();

        if (r1.getComodines() != null) {
            for (Integer i : r1.getComodines()) {
                if (i < idx) {
                    c1.add(i);
                }
                if (i >= idx) {
                    c2.add(i);
                }
            }
        }
        if (r2.getComodines() != null) {
            for (Integer i : r2.getComodines()) {
                if (i < idx) {
                    c2.add(i);
                }
                if (i >= idx) {
                    c1.add(i);
                }
            }
        }
        //System.out.println("Indice: " + idx);
        Regla rn1 = new Regla(bs1, (BitSet) r1.getAccion().clone(), (int) ((r1.getFitness() + r2.getFitness()) / 2), c1);
        Regla rn2 = new Regla(bs2, (BitSet) r2.getAccion().clone(), (int) ((r1.getFitness() + r2.getFitness()) / 2), c2);

        Regla[] vr = new Regla[2];
        vr[0] = rn1;
        vr[1] = rn2;
        return vr;
    }

    public static Regla mutarRegla(Regla r1, int longitudRegla) {
        int x = Utilidades.intAleatorio(1, 10);
        float porcentaje = ((0.3f / x) + (0.003f * x));
        int totalGenesAMutar = (int) Math.ceil(porcentaje * longitudRegla);

        Integer[] indicesGenesAMutar = Utilidades.vectorEnterosUnicosAleatorios(totalGenesAMutar, 0, longitudRegla);
        BitSet bs = (BitSet) r1.getCondicion().clone();

        List<Integer> comodines = Utilidades.copiarListaDeEnteros(r1.getComodines());

        for (Integer idx : indicesGenesAMutar) {
            bs.set(idx, !bs.get(idx));
            if (comodines.contains(idx)) {
                /*Si el indice es un comodin, se elimina*/
                for (int j = 0; j < comodines.size(); j++) {
                    if (idx == comodines.get(j)) {
                        comodines.remove(j);
                        break;
                    }
                }
            } else {
                /*Si el indice no es comodin, se decide si se agrega o no aleatoriamente*/
                XSRandom r = new XSRandom();
                if (r.nextBoolean()) {
                    comodines.add(idx);
                }
            }
        }
        Regla rn = new Regla(bs, (BitSet) r1.getAccion().clone(), r1.getFitness(), comodines);
        return rn;
    }

    public static List<Regla> seleccionarReglasPorRuleta(List<Regla> reglas, int cantidadDeReglasASeleccionar) {
        int totalReglas = reglas.size();
        int sumaFitness = 0;
        int[][] matriz = new int[totalReglas][2];

        for (int i = 0; i < totalReglas; i++) {
            sumaFitness += reglas.get(i).getFitness() + 1;
            matriz[i][0] = i;
            matriz[i][1] = sumaFitness;
        }

        XSRandom rand = new XSRandom();
        List<Regla> reglasSeleccionadas = new ArrayList();

        while (reglasSeleccionadas.size() < cantidadDeReglasASeleccionar) {
            int ruleta = rand.nextInt(sumaFitness + 1) + 1;
            for (int i = 0; i < matriz.length; i++) {
                if (matriz[i][1] >= ruleta) {
                    Regla rr = reglas.get(i);
                    if (!reglasSeleccionadas.contains(rr)) {
                        reglasSeleccionadas.add(rr);
                    }
                    break;
                }
            }
        }
        return reglasSeleccionadas;
    }

}
