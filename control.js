// Programa de control para búsqueda recurrente con profundidad progresiva
// 16 mayo 2015
// Carlos Grasa Lambea

var campoCuantoInicial; // interfaz para cuantoInicial
	var campoLimiteInicial; // interfaz para jugadaLimite
	var campoFormulaLimite; // interfaz para formulaLimite
	var campoUltimoEsGanador; // interfaz para ultimoEsGanador
	var campoProfundidadArbol; // interfaz para profundidadArbol
	var campoProfundidadEsProgresiva; // var interfaz para profundidadEsProgresiva
	var botonBuscar; // interfaz para botón buscar
	var campoResultado; // interfaz para resultado
	var cuantoInicial; // estado inicial de juego
	var jugadaLimite; // límite inicial de jugada
	var formulaLimite; // fórmula para calcular límite de jugada
	var ultimoEsGanador; // gana o no el último jugador
	var profundidadEsProgresiva; // es o no progresiva la profundidad
	var calcularLimite; // función para calcular límite actual según la jugada anterior n
	var nivel; // en cuál nivel actualmente
	var profundidadRecor; // máximo nivel alcanzado
	var totalNodos; // número de nodos evaluados
	var profundidadArbol; // limitación de niveles
	var profundidadActual; // tope actual de niveles
	var instante; // instante registrado

	addEventListener("load", iniciarInterfaz, false);
	function iniciarInterfaz() {
	campoCuantoInicial = document.getElementById("cuantoinicial");
		campoLimiteInicial = document.getElementById("limiteinicial");
		campoFormulaLimite = document.getElementById("formulalimite");
		campoUltimoEsGanador = document.getElementById("ultimoesganador");
		campoProfundidadEsProgresiva = document.getElementById("profundidadesprogresiva");
		campoProfundidadArbol = document.getElementById("profundidadarbol");
		botonBuscar = document.getElementById("botonbuscar");
		campoResultado = document.getElementById("resultado");
		botonBuscar.addEventListener("click", entrarDatos, false);
		return;
		}

function entrarDatos() {
campoResultado.innerHTML = "";
	cuantoInicial = campoCuantoInicial.value * 1;
	if (!isFinite(cuantoInicial) || cuantoInicial < 1 || Math.floor(cuantoInicial) < cuantoInicial) {
informar("Error en estado inicial.");
	return;
}
jugadaLimite = campoLimiteInicial.value * 1;
	if (!isFinite(jugadaLimite) || jugadaLimite < 1 || Math.floor(jugadaLimite) < jugadaLimite || jugadaLimite > cuantoInicial) {
informar("Error en jugada l&iacute;mite inicial.");
	return;
}
formulaLimite = campoFormulaLimite.value;
	calcularLimite = new Function("cuanto", "n", "var limite=" + formulaLimite + "; limite=Math.floor(limite); if (limite>cuanto) limite=cuanto; if (limite<0) limite=1; return limite;");
	var prueba = calcularLimite(1000000000, 1);
	if (!isFinite(prueba) || prueba < 1 || Math.floor(prueba) < prueba) {
informar("Error en c&aacute;lculo de liacute;mite.");
	return;
}
ultimoEsGanador = campoUltimoEsGanador.value === "ultimoesganador";
	profundidadArbol = campoProfundidadArbol.value * 1;
	if (!isFinite(profundidadArbol) || profundidadArbol < 1 || Math.floor(profundidadArbol) < profundidadArbol) {
informar("Error en l&iacute;mite de profundidad.");
	return;
}
profundidadEsProgresiva = campoProfundidadEsProgresiva.value === "profundidadesprogresiva";
	buscarSoluciones();
	return;
	}

function informar(dato) {
campoResultado.innerHTML += dato;
	return;
	}

function resumir(x) {
var n = Math.floor(Math.log(x) / Math.LN10 / 3);
	var y = new Number(x / Math.pow(10, 3 * n)).toFixed(1);
	switch (n) {
case 0:
	return y;
	break;
	case 1:
	return y + " K";
	break;
	case 2:
	return y + " M";
	break;
	case 3:
	return y + " G";
	break;
	case 4:
	return y + " T";
	break;
	case 5:
	return y + " P";
	break;
	default:
	return x;
}
}

function lapso() {
var ahora = new Date();
	var ms = ahora - instante;
	instante = ahora;
	return ms;
	}

function buscarSoluciones() {
    var duracion;
    var evaluacion;
    informar("Estado: " + cuantoInicial + ", l&iacute;mite: " + jugadaLimite + (ultimoEsGanador ? ", ganando" : ", perdiendo") + " quien juega &uacute;ltimo, profundidad " + (profundidadEsProgresiva ? "" : "no") + " progresiva del &aacute;rbol: " + profundidadArbol + ", f&oacutermula: f(n)=" + formulaLimite + "<br />Buscando jugadas ganadoras...<br />");
    for (var n = 1; n <= jugadaLimite; n++) {
	totalNodos = 0;
	lapso();
	for (profundidadActual = profundidadEsProgresiva ? 1 : profundidadArbol; profundidadActual <= profundidadArbol; profundidadActual++) {
	    nivel = 0;
	    profundidadRecor = 0;
	    evaluacion = evaluarNodo(cuantoInicial - n, calcularLimite(cuantoInicial-n,n));
	    if (evaluacion) {
		duracion = lapso();
		informar("<br />Buena jugada: tomar " + n);
		informar(", profundidad: " + profundidadRecor + " nivel" + (profundidadRecor === 1 ? "" : "es"));
		informar(", " + resumir(totalNodos) + " nodos en " + duracion + " ms");
		informar(duracion > 0 ? (", velocidad: " + resumir(totalNodos / duracion * 1000) + " nodos/segundo.") : ".");
		break;
	    }
	}
	if (!evaluacion) {
	    duracion = lapso() + 1;
	    informar("<br />Mala jugada: tomar " + n);
	    informar(", profundidad: " + profundidadRecor + " nivel" + (profundidadRecor === 1 ? "" : "es"));
	    informar(", " + resumir(totalNodos) + " nodos en " + duracion + " ms");
	    informar(duracion > 0 ? (", velocidad: " + resumir(totalNodos / duracion * 1000) + " nodos/segundo.") : ".");
	}
    }
    informar("<br /><br />B&uacute;squeda terminada.");
    return;
}

function evaluarNodo(cuanto, limite) { // cuanto: situación actual, limite: jugada límite
    var evaluacion = true;
    nivel++;
    totalNodos++;
    if (nivel > profundidadRecor)
	profundidadRecor = nivel;
    if (cuanto === (ultimoEsGanador ? 1 : 0))
	evaluacion = false;
    else if (limite + (ultimoEsGanador ? 0 : 1) >= cuanto)
	evaluacion = false;
    else if (nivel >= profundidadActual)
	evaluacion = nivel % 2 === 0;
    else
	for (var n = 1; n <= limite; n++)
	    if (evaluarNodo(cuanto - n, calcularLimite(cuanto - n, n))) {
		evaluacion = false;
		break;
	    }
    nivel--;
    return evaluacion;
}