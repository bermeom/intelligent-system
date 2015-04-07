package Tecnicas_IA.AlgoritmosGeneticos;

import Utilities.Utilidades;
import Utilities.XSRandom;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;

public class Regla {

    private BitSet condicion;
    private BitSet accion;
    private int fitness;
    private List<Integer> comodines;
    private int numeroActivaciones = 0;
    private int numeroAciertos = 0;
    private List<Regla> reglasPadres;
    private final boolean imprimirReglasPadre = false;
    private int generacion = 0;
    //private Timestamp ultimaActivacion;

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

    /*public void setCondicion(BitSet condicion) {
     this.condicion = condicion;
     }*/
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
        if (fitness < 0) {
            fitness = 0;
        }
        this.fitness = fitness;
    }

    public List<Integer> getComodines() {
        return comodines;
    }

    public int getNumeroActivaciones() {
        return numeroActivaciones;
    }

    public void setNumeroActivaciones(int numeroActivaciones) {
        this.numeroActivaciones = numeroActivaciones;
    }

    public int getNumeroAciertos() {
        return numeroAciertos;
    }

    public void setNumeroAciertos(int numeroAciertos) {
        this.numeroAciertos = numeroAciertos;
    }

    public int getGeneracion() {
        return generacion;
    }

    public void setGeneracion(int generacion) {
        this.generacion = generacion;
    }
    
    public void incrementarGeneracion() {
        this.generacion++;
    }
    
    public void sumarActivacion() {
        this.numeroActivaciones++;
    }

    public void sumarAciertos() {
        this.numeroAciertos++;
    }

    public float getEspecificidad(int longitudCromosoma) {
        if (this.comodines == null) {
            return 1;
        } else {
            return (float) (longitudCromosoma - this.comodines.size()) / longitudCromosoma;
        }
    }

    public List<Regla> getReglasPadres() {
        return reglasPadres;
    }

    public void setReglasPadres(List<Regla> reglas) {
        this.reglasPadres = reglas;
    }
    /*public void setComodines(List<Integer> comodines) {
     this.comodines = comodines;
     }*/

    public String toDebugString() {
        return toDebugString(-1, -1);
    }

    
    public String toDebugString(int tamañoCondicion, int tamañoAccion) {
        return toDebugString(tamañoCondicion, tamañoAccion,true);
    }
    
    private String toDebugString(int tamañoCondicion, int tamañoAccion, boolean imprimirReglasPadre) {
        String f = "C: %s  A: %s  F: %s  #: %s  |  NumAct: %s  NumAciert: %s  Generación: %s  ";
        if (this.imprimirReglasPadre) {
            f += "  |  Padres: %s";
        }

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
        
        String rPadres = "";
        if (this.imprimirReglasPadre) {
            
            if (this.reglasPadres != null) {
                for (Regla r : this.reglasPadres) {
                    rPadres += r.toDebugString(tamañoCondicion, tamañoAccion, false);
                }
            }
        }

        return String.format(f, sC, sA, this.fitness, sComodines, this.numeroActivaciones, this.numeroAciertos, this.generacion ,rPadres);
    }

    /*public static String toDebugString(BitSet b) {
     String s = "";
     long[] ab = b.toLongArray();
     if (ab != null && ab.length > 0) {
     for (int i = ab.length - 1; i >= 0; i--) {
     //s += Long.toString(ab[i], 2);
     s += Long.toBinaryString(ab[i]);
     }
     }
     return s;
     }*/
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
