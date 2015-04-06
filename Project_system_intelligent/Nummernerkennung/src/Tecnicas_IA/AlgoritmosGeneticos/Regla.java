package Tecnicas_IA.AlgoritmosGeneticos;

import Utilities.Utilidades;
import Utilities.XSRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;

/*
 * @author Juan
 */
public class Regla {

    private BitSet condicion;
    private BitSet accion;
    private int fitness;
    private List<Integer> comodines;

    public Regla(BitSet condicion, BitSet accion, int fitness, List<Integer> comodines) {

        if (condicion == null) {
            throw new NullPointerException("La condición de la regla no puede ser nula.");
        }
        if (accion == null) {
            throw new NullPointerException("La acción de la regla no puede ser nula.");
        }
        if (condicion.size() == 0) {
            throw new NullPointerException("La condición debe tener una longitud mayor a cero");
        }
        if (accion.size() == 0) {
            throw new NullPointerException("La acción debe tener una longitud mayor a cero");
        }

        if (comodines != null && comodines.size() > 0) {
            for (int i = 0; i < comodines.size(); i++) {
                if (comodines.get(i) < 0) {
                    comodines.remove(i);
                }
            }
            /*elimina duplicados*/
            comodines = new ArrayList(new HashSet(comodines));
        }

        this.condicion = condicion;
        this.accion = accion;
        this.fitness = fitness;
        this.comodines = comodines;
    }

    public BitSet getCondicion() {
        return condicion;
    }

    public void setCondicion(BitSet condicion) {
        this.condicion = condicion;
    }

    public BitSet getAccion() {
        return accion;
    }

    public void setAccion(BitSet accion) {
        this.accion = accion;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public List<Integer> getComodines() {
        return comodines;
    }

    public void setComodines(List<Integer> comodines) {
        this.comodines = comodines;
    }

    public String toDebugString() {
        return toDebugString(-1, -1);
    }

    public String toDebugString(int tamañoCondicion, int tamañoAccion) {
        String f = "C: %s  A: %s  F: %s  #: %s";

        String sC = Regla.toDebugString(this.condicion);

        if (tamañoCondicion > 0) {
            sC = sC.substring(sC.length() - tamañoCondicion);
        }

        if (sC.length() > 0) {
            /*reemplazo de comodines*/
            if (this.comodines != null && this.comodines.size() > 0) {
                char[] buf = sC.toCharArray();
                int longitudCondicion = buf.length;
                for (Integer comodine : this.comodines) {
                    int idx = comodine;
                    if (idx <= longitudCondicion - 1) {
                        buf[longitudCondicion - 1 - idx] = '#';
                    }
                }
                sC = new String(buf);
            }
        }
        String sA = Regla.toDebugString(this.accion);

        if (tamañoAccion > 0) {
            sA = sA.substring(sA.length() - tamañoAccion);
        }

        int sComodines = 0;
        if (this.comodines != null) {
            sComodines = this.comodines.size();
        }

        return String.format(f, sC, sA, this.fitness, sComodines);
    }

    public static String toDebugString(BitSet b) {
        int size = b.size();
        char[] buf = new char[size];
        Arrays.fill(buf, '0');
        for (int i = b.nextSetBit(0); i >= 0; i = b.nextSetBit(i + 1)) {
            buf[size - 1 - i] = '1';
        }
        return new String(buf);
    }

    public static BitSet crearBitsetDesdeString(final String input) {
        final int length = input.length();
        final BitSet bitSet = new BitSet(length);
        for (int i = length - 1; i >= 0; i--) {
            if (input.charAt(i) == '1') {
                bitSet.set(length - 1 - i);
            }
        }
        return bitSet;
    }

    public static BitSet crearBitSetAleatorio(int longitud) {
        int iteraciones = longitud / Long.SIZE;
        int residuo = longitud - (iteraciones * Long.SIZE);
        BitSet bitSet;

        XSRandom rand = new XSRandom();
        long[] larray = new long[iteraciones];
        for (int i = 0; i < iteraciones; i++) {
            larray[i] = rand.nextLong();
        }
        bitSet = BitSet.valueOf(larray);

        if (bitSet == null || bitSet.size() == 0) {
            bitSet = new BitSet();
        }

        if (residuo > 0) {
            String str = Long.toBinaryString(rand.nextLong());
            for (int i = 0; i < str.length(); i++) {
                residuo--;
                if (str.charAt(str.length() - 1 - i) == '1') {
                    bitSet.set((iteraciones * Long.SIZE) + i);
                }
                if (residuo == 0) {
                    break;
                }
            }
        }
        return bitSet;
    }

    public static Regla crearReglaAleatoria(int longitudCondicion) {
        BitSet condicion = crearBitSetAleatorio(longitudCondicion);
        BitSet accion = crearBitsetDesdeString(Utilidades.intToBinaryString(Utilidades.intAleatorio(0, 9)));
        int fitness = Utilidades.intAleatorio(0, 100);
        List<Integer> comodines = Arrays.asList(Utilidades.vectorEnterosAleatorio(longitudCondicion, 0, longitudCondicion - 1));
        return new Regla(condicion, accion, fitness, comodines);
    }

    public boolean verificarActivacion(BitSet input) {
        boolean ok = false;
        if (input != null) {
            BitSet auxInput = (BitSet) input.clone();
            if (this.comodines != null && this.comodines.size() > 0) {
                for (Integer comodine : this.comodines) {
                    int idx = comodine;
                    auxInput.set(idx, this.condicion.get(idx));
                }
            }
            if (auxInput.equals(this.condicion)) {
                ok = true;
            }
        }
        return ok;
    }
}
