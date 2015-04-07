package Tecnicas_IA.AlgoritmosGeneticos;

import Agente.SensorCamara;
import static Tecnicas_IA.AlgoritmosGeneticos.Regla.crearBitSetAleatorio;
import static Tecnicas_IA.AlgoritmosGeneticos.Regla.crearBitsetDesdeString;
import Utilities.Mensaje;
import Utilities.Utilidades;
import Utilities.XSRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LCS {

    private int numeroReglas = 20;
    private float parametroAprendizaje = 0.15f;
    private int longitudCromosoma;
    private final int iteracionesEjecucionEvolucion = 20;
    private final int numeroEvolucionesPorIteracion = 10;
    private final int numeroMaximoIteraciones = 10000;
    private List<Regla> reglas;
    private SensorCamara sensor;
    private final int valorRecompensa = 10;
    private final boolean imprimirReglas = false;

    public LCS() {

    }

    public void iniciarAprendizaje() {

        try {
            sensor = new SensorCamara(SensorCamara.TipoSensor.Entrenamiento);
        } catch (Exception ex) {
            System.out.println("Se produjo el siguiente error al incializar el sensor. " + ex.toString());
        }
        if (sensor != null) {
            Mensaje m = sensor.ObtenerMensaje();
            configuracionInicial(m);
            ejecutarClasificador();
        }
    }

    public void iniciarEtapaValidacion() {
        try {
            sensor = new SensorCamara(SensorCamara.TipoSensor.Validacion);
        } catch (Exception ex) {
            System.out.println("Se produjo el siguiente error al incializar el sensor. " + ex.toString());
        }
        if (sensor != null) {
            ejecutarValidacion();
        }
    }

    private void configuracionInicial(Mensaje m) {
        this.longitudCromosoma = m.mensaje.length();
        this.reglas = new ArrayList<Regla>();
        int numeroReglasIniciales = Math.round(this.numeroReglas * 0.7f);
        for (int i = 0; i < numeroReglasIniciales; i++) {
            agregarNuevaRegla(Regla.crearReglaAleatoria(this.longitudCromosoma));
        }
    }

    private boolean continuarIteraciones(int iteracion) {
        return iteracion <= numeroMaximoIteraciones && !sensor.finArchivo();
    }

    private void ejecutarClasificador() {
        int iteracion = 1;
        imprimirReglas();
        while (continuarIteraciones(iteracion)) {
            System.out.println("");
            System.out.print("Iteracion: " + iteracion + "  TotalReglas: " + this.reglas.size());
            Mensaje m = m = sensor.ObtenerMensaje();
            if (m != null) {

                BitSet b = Regla.crearBitsetDesdeString(m.mensaje);
                List<Regla> reglasActivadas = obtenerReglasActivadas(b);
                if (reglasActivadas == null || reglasActivadas.size() == 0) {
                    reglasActivadas = generarReglaAleatoriaPreActivada(m);
                }

                for (Regla r : reglasActivadas) {
                    r.sumarActivacion();
                }

                Regla rAccion = SeleccionarReglaPorApuestas(reglasActivadas, this.parametroAprendizaje, this.longitudCromosoma);
                if (rAccion != null) {
                    int numeroPredecido = Utilidades.convertirBitSetEnInt(rAccion.getAccion());
                    administrarRecompensa(m.numero, numeroPredecido, rAccion);

                    EscribirDepuracion(m, reglasActivadas, numeroPredecido);
                }

                /*Cada x iteraciones se evolucionan las reglas*/
                if ((iteracion % this.iteracionesEjecucionEvolucion) == 0) {
                    if (imprimirReglas) {
                        imprimirReglas(2);
                    }

                    evolucionarReglas();

                    if (imprimirReglas) {
                        System.out.println("\r\nReglas evolucionadas:");
                        imprimirReglas(2);
                    }
                }
            }
            iteracion++;
            //Utilidades.esperarEntradaDeTeclado();
        }
        System.out.println("**************FIN**************");
        imprimirReglas();
    }

    private void ejecutarValidacion() {
        int iteracion = 1;
        int resultados[][] = new int[10][10];
        int sinClasificar = 0;
        while (continuarIteraciones(iteracion)) {

            Mensaje m = m = sensor.ObtenerMensaje();
            if (m != null) {

                BitSet b = Regla.crearBitsetDesdeString(m.mensaje);
                List<Regla> reglasActivadas = obtenerReglasActivadas(b);

                Regla rAccion = SeleccionarReglaPorApuestas(reglasActivadas, this.parametroAprendizaje, this.longitudCromosoma);
                if (rAccion != null) {
                    int numeroPredecido = Utilidades.convertirBitSetEnInt(rAccion.getAccion());
                    resultados[m.numero][numeroPredecido]++;
                }
                else
                    sinClasificar++;

            }
            iteracion++;
        }
        System.out.println("");
        System.out.println("**************FIN**************");
        System.out.println("TABLA DE RESULTADOS");

        for (int i = 0; i < resultados.length; i++) {
            int[] fila = resultados[i];
            if (i == 0) {
                System.out.println("Números \t vs \t predicciones");
                System.out.print("\t");
                for (int j = 0; j < fila.length; j++) {
                    System.out.print("(" + j + ")" + "\t");
                }
                System.out.print("\n");
            }
            System.out.print("(" + i + ")" + "\t");
            for (int j = 0; j < fila.length; j++) {

                System.out.print(fila[j] + "\t");
            }
            System.out.print("\n");
        }
        
        System.out.println("\n Sin clasificar: " + sinClasificar );
        
        System.out.println("\n\n ESTADISTICAS");
        for (int i = 0; i < resultados.length; i++) {
            int[] fila = resultados[i];
            System.out.print("(" + i + ")" + "\t");
            int sumaTotal = 0;
            int sumaOk = 0;
            int sumaError = 0;
            for (int j = 0; j < fila.length; j++) {
                sumaTotal += fila[j];
                if (i == j) {
                    sumaOk += fila[j];
                } else {
                    sumaError += fila[j];
                }
            }
            String str = "SumaTotal: %s \t\t Ok: %s \t\t Error: %s \t\t  OK  %s \t\t Error %s ";
            System.out.print(String.format(str, sumaTotal, sumaOk, sumaError, (float) sumaOk / sumaTotal * 100, (float) sumaError / sumaTotal * 100));
            System.out.print("\n");
        }

    }

    private void EscribirDepuracion(Mensaje m, List<Regla> reglas, int prediccion) {
        System.out.println("\r\n*** Mensaje: " + m.mensaje);
        for (Regla r : reglas) {
            System.out.println(" -> " + r.toDebugString(this.longitudCromosoma, 4));
        }
        System.out.println(" -> Numero real: " + m.numero + " Numero predecido: " + prediccion);
    }

    private List<Regla> obtenerReglasActivadas(BitSet bitSetMensaje) {
        List<Regla> reglasActivadas = new ArrayList<Regla>();
        for (Regla r : this.reglas) {
            if (r.verificarActivacion(bitSetMensaje)) {
                reglasActivadas.add(r);
            }
        }
        System.out.print("  " + "Reglas activadas: " + reglasActivadas.size());
        return reglasActivadas;
    }

    private List<Regla> generarReglaAleatoriaPreActivada(Mensaje m) {
        List<Regla> reglasActivadas = new ArrayList<Regla>();
        BitSet condicion = crearBitsetDesdeString(m.mensaje);
        BitSet accion = crearBitsetDesdeString(Utilidades.intToBinaryString(Utilidades.intAleatorio(0, 9)));
        int fitness = Utilidades.intAleatorio(0, 100);
        List<Integer> comodines = Arrays.asList(Utilidades.vectorEnterosAleatorio(m.mensaje.length(), 0, m.mensaje.length() - 1));
        Regla r = new Regla(condicion, accion, fitness, comodines);
        agregarNuevaRegla(r);
        reglasActivadas.add(r);
        return reglasActivadas;
    }

    public static Regla SeleccionarReglaPorApuestas(List<Regla> reglas, float parametroAprendizaje, int longitudCromosoma) {
        Regla r = null;
        int idx = 0;
        float apuestaMayor = 0f;
        if (reglas.size() > 0) {
            for (int i = 0; i < reglas.size(); i++) {
                float apuesta = parametroAprendizaje * reglas.get(i).getFitness() * reglas.get(i).getEspecificidad(longitudCromosoma);
                if (apuesta > apuestaMayor) {
                    idx = i;
                    apuestaMayor = apuesta;
                }
            }
            r = reglas.get(idx);
        }
        return r;
    }

    private Regla SeleccionarReglaConMayorFitness(List<Regla> reglas) {
        Regla r = null;
        if (reglas != null && reglas.size() > 0) {
            r = reglas.get(0);
            for (Regla i : reglas) {
                if (r.getFitness() < i.getFitness()) {
                    r = i;
                }
            }
        }
        return r;
    }

    private Regla SeleccionarReglaConMenorFitness(List<Regla> reglas) {
        Regla r = null;
        if (reglas != null && reglas.size() > 0) {
            r = reglas.get(0);
            for (Regla i : reglas) {
                if (r.getFitness() > i.getFitness()) {
                    r = i;
                }
            }
        }
        return r;
    }

    private Regla SeleccionarReglaConMenosAciertos(List<Regla> reglas) {
        Regla r = null;
        if (reglas != null && reglas.size() > 0) {
            r = reglas.get(0);
            for (Regla i : reglas) {
                if (r.getNumeroAciertos() > i.getNumeroAciertos()) {
                    r = i;
                }
            }
        }
        return r;
    }

    private void administrarRecompensa(int numeroReal, int numeroPredecido, Regla reglaAccion) {
        int recompensa = valorRecompensa;
        if (numeroReal == numeroPredecido) {
            reglaAccion.sumarAciertos();
        } else {
            recompensa = recompensa * -1;
        }
        System.out.print("  " + (recompensa > 0 ? "BIIIIIIIEEEEEENNNNNNNN!!!!!!!!! $$$$$$$ " : "Mallllll"));
        reglaAccion.setFitness(reglaAccion.getFitness() + recompensa);
    }

    private void agregarNuevaRegla(Regla r) {
        if (this.reglas.size() < this.numeroReglas) {
            this.reglas.add(r);
        } else {
            Regla rdelete = SeleccionarReglaConMenosAciertos(this.reglas);
            this.reglas.remove(rdelete);
            this.reglas.add(r);
        }
    }

    public static List<Regla> recombinarReglas(Regla r1, Regla r2, int longitudRegla) {
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

        List<Regla> nReglas = new ArrayList();
        nReglas.add(rn1);
        nReglas.add(rn2);
        return nReglas;
    }

    public static Regla mutarRegla(Regla r1, int longitudRegla) {
        int x = Utilidades.intAleatorio(1, 10);
        float porcentaje = 0.1f;//((0.3f / x) + (0.003f * x));
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

        BitSet accion = (BitSet) r1.getAccion().clone();

        accion = crearBitsetDesdeString(Utilidades.intToBinaryString(Utilidades.intAleatorio(0, 9)));

        Regla rn = new Regla(bs, accion, r1.getFitness(), comodines);
        return rn;
    }

    public static List<Regla> seleccionarReglasPorRuleta(List<Regla> reglas, int cantidadDeReglasASeleccionar) {
        int totalReglas = reglas.size();
        int sumaFitness = 0;
        int[][] matriz = new int[totalReglas][2];

        for (int i = 0; i < totalReglas; i++) {
            sumaFitness += reglas.get(i).getFitness()+ 1;
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

    public void evolucionarReglas() {
        List<Regla> copiaReglas = new ArrayList<Regla>(this.reglas);

        for (int i = 0; i < numeroEvolucionesPorIteracion; i++) {
            List<Regla> r = seleccionarReglasPorRuleta(copiaReglas, 2);
            List<Regla> vc = recombinarReglas(r.get(0), r.get(1), this.longitudCromosoma);
            for (int j = 0; j < vc.size(); j++) {
                Regla rx = mutarRegla(vc.get(j), this.longitudCromosoma);

                List<Regla> reglasPadres = new ArrayList();
                reglasPadres.add(r.get(0));
                reglasPadres.add(r.get(1));
                rx.setReglasPadres(reglasPadres);
                rx.setGeneracion(((int) Math.ceil((r.get(0).getGeneracion() + r.get(1).getGeneracion()) / 2.0)) + 1);

                vc.set(j, rx);
                this.agregarNuevaRegla(vc.get(j));
            }
        }
    }

    private void imprimirReglas() {
        imprimirReglas(0);
    }

    private void imprimirReglas(int tabs) {
        System.out.println("");
        String i = "";
        for (int j = 0; j < tabs; j++) {
            i += "\t";
        }
        for (int x = 0; x < this.reglas.size(); x++) {
            System.out.println(i + x + ") " + "Debug Regla: " + this.reglas.get(x).toDebugString(this.longitudCromosoma, 4));
        }
    }
}
