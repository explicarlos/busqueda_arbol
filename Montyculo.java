// programa: Búsqueda recursiva en árbol alternado para juego del montículo
// versión: 20240515
// autor: Carlos Grasa Lambea
public class Montyculo { // Análisis de jugadas en árbol alternado (minimax)
    // constructor --------------------------------------------------------
    public Montyculo() { // constructor predeterminado
        return;
    }

    // campos -------------------------------------------------------------
    private int cuantoInicial; // estado inicial del tablero de juego
    private int jugadaLimite; // límite inicial de jugada
    private int formulaLimiteLineal; // coeficiente lineal del cálculo de jugada límite
    private int formulaLimiteConstante; // coeficiente constante del cálculo de jugada límite
    private boolean esGanadorUltimo; // gana o no la partida quien mueve el último
    private boolean esProfundidadProgresiva; // es o no la búsqueda primero en anchura
    private int nivel; // nivel de profundidad actual
    private int profundidadRecor; // máximo nivel de profundidad alcanzado actualmente
    private int totalNodos; // número de nodos evaluados
    private int profundidadArbol; // límite establecido para la profundidad de la búsqueda
    private int profundidadActual; // límite actual de profundidad progresiva para búsqueda primero en anchura
    private long instante; // instante registrado para cálculo de intervalos temporales

    // método estático main ---------------------------------------------------
    public static void main(String[] args) {
        try {
            Montyculo app=new Montyculo();
            app.controlar();
            K.escribir("- Programa terminado normalmente.\n");
            K.pausarConsola();
            System.exit(0);
        } catch (Exception exc) {
            K.escribir("- Error surgido durante ejecución. Abortando programa.\n");
            K.escribir(exc.getMessage()+"\n");
            exc.printStackTrace();
            System.exit(1);
        }
        return;
    }

    // métodos ----------------------------------------------------------------
    public void controlar() { // programa ejecutor de la app
        do {
            entrarDatos();
            buscarSoluciones();
        } while (K.preguntarChar("¿Desea realizar otro análisis?", "sn")=='s');
        return;
    }
    public void entrarDatos() { // pide los datos del juego para la búsqueda
        K.limpiarConsola();
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("                 Análisis de jugadas para el juego del montón\n");
        K.escribir("                             Introducción de datos\n");
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("Descripción del juego:\n");
        K.escribir("Hay un montón que contiene un número inicial de puntos.\n");
        K.escribir("Dos jugadores van retirando, por turno, algunos puntos del montón desde uno hasta\n");
        K.escribir("un máximo que depende de la jugada anterior.\n");
        K.escribir("Ganará la partida (o la perderá, a elegir) quien realice el último movimiento\n");
        K.escribir("dejando vacío el montón.\n");
        K.escribir("------------------------------------------------------------------------------------------\n");
        cuantoInicial=(int) K.preguntarLong("Introduzca los puntos que hay en el montón actualmente (1-50)", 1, 50);
        K.escribir("------------------------------------------------------------------------------------------\n");
        jugadaLimite=(int) K.preguntarLong("Introduzca el límite de puntos a retirar en la primera jugada (1-"+cuantoInicial+")", 1, cuantoInicial);
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("En cada turno la jugada máxima a realizar dependerá de cuántos puntos se retiraron\n");
        K.escribir("en la jugada anterior. Siendo 'n' los puntos retirados en la jugada precedente, el límite\n");
        K.escribir("de puntos a retirar en la siguiente jugada se calcula como: a*n+b, siendo 'a' y 'b'\n");
        K.escribir("dos parámetros a escoger.\n");
        formulaLimiteLineal=(int) K.preguntarLong("Introduzca el parámetro lineal 'a'", 0, 5);
        K.escribir("------------------------------------------------------------------------------------------\n");
        formulaLimiteConstante=(int) K.preguntarLong("Introduzca el parámetro constante 'b'", 0, 5);
        K.escribir("------------------------------------------------------------------------------------------\n");
        esGanadorUltimo=K.preguntarChar("Indique si es o no ganador quien juegue el último turno", "sn")=='s';
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("La búsqueda de mejor jugada puede hacerse de dos maneras: buscando prioritariamente\n");
        K.escribir("el árbol en anchura aumentando la profundidad progresivamente (primero en anchura),\n");
        K.escribir("o bien priorizando la búsqueda en las ramas más profundas (primero en profundidad).\n");
        esProfundidadProgresiva=K.preguntarChar("Indique si la búsqueda es o no primero en anchura", "sn")=='s';
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("Para no entrar en una búsqueda eterna de soluciones, se limitará la profundidad máxima\n");
        K.escribir("de jugadas a explorar en el árbol.\n");
        profundidadArbol=(int) K.preguntarLong("Indique el máximo nivel de profundidad de exploración", 1, cuantoInicial+2);
        K.limpiarConsola();
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("                 Análisis de jugadas para el juego del montón\n");
        K.escribir("                 Estado inicial para la búsqueda de soluciones\n");
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("                  - tamaño del montón:      "+cuantoInicial+" puntos\n");
        K.escribir("              - jugada máxima inicial:      retirar "+jugadaLimite+" puntos\n");
        K.escribir("- máxima jugada, según precedente 'n':      retirar ");
        if (formulaLimiteLineal>0) {
            K.escribir(formulaLimiteLineal + "*n");
            if (formulaLimiteConstante>0)
                K.escribir("+"+formulaLimiteConstante);
        } else
            K.escribir(""+formulaLimiteConstante);
        K.escribir(" puntos\n");
        K.escribir("              - ganador de la partida:      quien "+(esGanadorUltimo ? "" : "no ")+"juegue último\n");
        K.escribir("          - estrategia de exploración:      primero en "+(esProfundidadProgresiva ? "anchura" : "profundidad")+"\n");
        K.escribir("  - profundidad máxima de exploración:      "+profundidadArbol+" niveles\n");
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("A continuación se iniciará la exploración de soluciones. ");
        K.pausarConsola();
        return;
    }
    public int calcularLimite(int estadoActual, int jugadaAnterior) { // calcula la jugada máxima realizable
        int limite=formulaLimiteLineal*jugadaAnterior+formulaLimiteConstante;
        return Integer.min(estadoActual, Integer.max(1, formulaLimiteLineal*jugadaAnterior+formulaLimiteConstante));
    }
    public String resumirPrefifos(long x) {
        int n=(int) Math.floor(Math.log10(x)/3);
        double y=Math.floor(x/Math.pow(10, 3*n)*10)/10;
        switch (n) {
            case 0 -> { return ""+y; }
            case 1 -> { return y+" K"; }
            case 2 -> { return y+" M"; }
            case 3 -> { return y+" G"; }
            case 4 -> { return y+" T"; }
            case 5 -> { return y+" P"; }
            default -> { return ""+x; }
        }
    }
    public long registrarLapso() {
        long antes=instante;
        instante=System.currentTimeMillis();
        return instante-antes;
    }
    public boolean evaluarNodo(int cuanto, int limite) { // decide si un nodo es o no ganador, cuanto: situación actual, limite: jugada límite
        nivel++;
        totalNodos++;
        if (nivel > profundidadRecor)
            profundidadRecor = nivel;
        if (cuanto == (esGanadorUltimo ? 1 : 0) || limite + (esGanadorUltimo ? 0 : 1) >= cuanto)
            return false;
        if (nivel >= profundidadActual)
            return nivel % 2 == 0;
        for (int n = 1; n <= limite; n++)
            if (evaluarNodo(cuanto - n, calcularLimite(cuanto - n, n)))
                return false;
        nivel--;
        return true;
    }
    public void buscarSoluciones() {
        long duracion;
        boolean evaluacion=false;
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("Analizando posibles jugadas...\n");
        for (var n = 1; n <= jugadaLimite; n++) {
            totalNodos = 0;
            profundidadRecor=0;
            nivel=0;
            registrarLapso();
            for (profundidadActual = esProfundidadProgresiva ? 1 : profundidadArbol; profundidadActual <= profundidadArbol; profundidadActual++) {
                nivel = 0;
                profundidadRecor = 0;
                evaluacion = evaluarNodo(cuantoInicial - n, calcularLimite(cuantoInicial-n,n));
                if (evaluacion) {
                    duracion = registrarLapso();
                    K.escribir("- Encontrada jugada  GANADORA: tomar " + n+" puntos");
                    K.escribir(", profundidad: " + profundidadRecor + " nivel" + (profundidadRecor == 1 ? "" : "es"));
                    K.escribir(", " + resumirPrefifos(totalNodos) + " nodos en " + duracion + " ms");
                    K.escribir(duracion > 0 ? (", velocidad: " + resumirPrefifos((long) (1000.0*totalNodos / duracion)) + "nodos/segundo.\n") : ".\n");
                    break;
                }
            }
            if (!evaluacion) {
                duracion = registrarLapso();
                K.escribir("- Encontrada jugada PERDEDORA: tomar " + n+" puntos");
                K.escribir(", profundidad: " + profundidadRecor + " nivel" + (profundidadRecor == 1 ? "" : "es"));
                K.escribir(", " + resumirPrefifos(totalNodos) + " nodos en " + duracion + " ms");
                K.escribir(duracion > 0 ? (", velocidad: " + resumirPrefifos((long) (1000.0*totalNodos / duracion)) + "nodos/segundo.\n") : ".\n");
            }
        }
        K.escribir("------------------------------------------------------------------------------------------\n");
        K.escribir("- Terminada la búsqueda de jugadas. ");
        return;
    }

}
