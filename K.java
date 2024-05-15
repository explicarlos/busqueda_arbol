// K.java
// plantilla modelo para app java de consola
// versión: 20240419
// autor: Carlos Grasa Lambea

import java.io.*;

public final class K {
	static String prontuario = " -> ";

	static void escribir(String texto) {
		System.out.print(texto);
		return;
	}

	static String leer() {
		String entrada = "";
		try {
			int car;
			car = System.in.read();
			while (car != '\n') {
				entrada += (char) car;
				car = System.in.read();
			}
		} catch (Exception exc) {
			entrada = "";
		}
		entrada = entrada.replace("\r", "");
		return entrada;
	}

	static String preguntar(String pregunta) {
		escribir(pregunta + prontuario);
		return (leer());
	}

	static char leerChar() {
		String entrada = leer();
		return entrada.isEmpty() ? '\0' : entrada.charAt(0);
	}

	static char preguntarChar(String pregunta) {
		char respuesta;
		do {
			escribir(pregunta + prontuario);
			respuesta = (leerChar() + " ").toLowerCase().charAt(0);
			if (respuesta == '\0')
				escribir("Debe responder un solo carácter\n");
		} while (respuesta == '\0');
		return respuesta;
	}

	static char preguntarChar(String pregunta, String opciones) {
		char respuesta;
		if (opciones.isEmpty())
			return '\0';
		opciones = opciones.toLowerCase();
		do {
			escribir(pregunta + " (" + opciones.charAt(0));
			for (int n = 1; n < opciones.length(); n++)
				escribir("/" + opciones.charAt(n));
			escribir(")" + prontuario);
			respuesta = (leerChar() + " ").toLowerCase().charAt(0);
			if (respuesta == '\0')
				escribir("Debe responder un solo carácter\n");
			if (!opciones.contains("" + respuesta))
				escribir("Debe escoger una opción válida.\n");
		} while (!opciones.contains("" + respuesta));
		return respuesta;
	}

	static long leerLong() {
		long salida;
		String entrada = leer();
		try {
			salida = Long.parseLong(entrada);
		} catch (Exception exc) {
			salida = Long.MIN_VALUE;
		}
		return salida;
	}

	static long leerLong(long cotaInf) {
		long entrada = leerLong();
		return entrada < cotaInf ? Long.MIN_VALUE : entrada;
	}

	static long leerLong(long cotaInf, long cotaSup) {
		if (cotaInf > cotaSup) {
			long aux = cotaInf;
			cotaInf = cotaSup;
			cotaSup = aux;
		}
		long entrada = leerLong(cotaInf);
		return entrada > cotaSup ? Long.MIN_VALUE : entrada;
	}

	static double leerDouble() {
		double salida;
		String entrada = leer();
		try {
			salida = Double.parseDouble(entrada);
		} catch (Exception exc) {
			salida = Double.MIN_VALUE;
		}
		return salida;
	}

	static Double leerDouble(double cotaInf) {
		double entrada = leerDouble();
		return entrada < cotaInf ? Double.MIN_VALUE : entrada;
	}

	static Double leerDouble(double cotaInf, double cotaSup) {
		if (cotaInf > cotaSup) {
			double aux = cotaInf;
			cotaInf = cotaSup;
			cotaSup = aux;
		}
		double entrada = leerDouble(cotaInf);
		return entrada > cotaSup ? Double.MIN_VALUE : entrada;
	}

	static long preguntarLong(String pregunta) {
		long entrada;
		do {
			escribir(pregunta + prontuario);
			entrada = leerLong();
			if (entrada == Long.MIN_VALUE)
				escribir("Debe responder un número entero válido.\n");
		} while (entrada == Long.MIN_VALUE);
		return entrada;
	}

	static long preguntarLong(String pregunta, long cotaInf) {
		long entrada;
		do {
			escribir(pregunta + prontuario);
			entrada = leerLong();
			if (entrada < cotaInf)
				escribir("Debe responder un entero desde " + cotaInf + " como mínimo.\n");
		} while (entrada < cotaInf);
		return entrada;
	}

	static long preguntarLong(String pregunta, long cotaInf, long cotaSup) {
		long entrada;
		if (cotaInf > cotaSup) {
			long aux = cotaInf;
			cotaInf = cotaSup;
			cotaSup = aux;
		}
		do {
			escribir(pregunta + prontuario);
			entrada = leerLong();
			if (entrada < cotaInf || entrada > cotaSup)
				escribir("Debe responder un entero entre " + cotaInf + " y " + cotaSup + " como intervalo válido.\n");
		} while (entrada < cotaInf || entrada > cotaSup);
		return entrada;
	}

	static Double preguntarDouble(String pregunta) {
		Double entrada;
		do {
			escribir(pregunta + prontuario);
			entrada = leerDouble();
			if (entrada == Double.MIN_VALUE)
				escribir("Debe responder un número válido.\n");
		} while (entrada == Double.MIN_VALUE);
		return entrada;
	}

	static double preguntarDouble(String pregunta, double cotaInf) {
		double entrada;
		do {
			escribir(pregunta + prontuario);
			entrada = leerDouble();
			if (entrada < cotaInf)
				escribir("Debe responder un entero desde " + cotaInf + " como mínimo.\n");
		} while (entrada < cotaInf);
		return entrada;
	}

	static double preguntarDouble(String pregunta, double cotaInf, double cotaSup) {
		double entrada;
		if (cotaInf > cotaSup) {
			double aux = cotaInf;
			cotaInf = cotaSup;
			cotaSup = aux;
		}
		do {
			escribir(pregunta + prontuario);
			entrada = leerDouble();
			if (entrada < cotaInf || entrada > cotaSup)
				escribir("Debe responder un número entre " + cotaInf + " y " + cotaSup + " como intervalo válido.\n");
		} while (entrada < cotaInf || entrada > cotaSup);
		return entrada;
	}
	static void limpiarConsola() {
		escribir("\n".repeat(40));
		return;
	}
	static void pausarConsola() {
		preguntar("Pulse INTRO para continuar");
		return;
	}
	static String[] listarDirectorio(String ruta) { // devuelve lista de los elementos de un directorio
		java.io.File archivo = new java.io.File(ruta);
		if (archivo.exists())
			return archivo.list();
		else
			return new String[0];
	}
	static String getPropiedadesArchivo(String ruta) { // devuelve las propiedades de un archivo
		java.io.File archivo = new java.io.File(ruta);
		String propiedades = "";
		if (archivo.exists()) {
			propiedades += archivo.isDirectory() ? "d" : "";
			if (archivo.isFile()) {
				propiedades += "a";
				propiedades += archivo.length() == 0 ? "0" : "";
			}
			propiedades += archivo.canExecute() ? "x" : "";
			propiedades += archivo.canRead() ? "r" : "";
			propiedades += archivo.canWrite() ? "w" : "";
			propiedades += archivo.isHidden() ? "h" : "";
		}
		return propiedades;
	}
	static boolean guardarObjeto(Object objeto, String archivo) { // serializa un objeto y lo guarda en archivo
		try {
			File fil = new File(archivo);
			if (!fil.exists() || !fil.isFile() || !fil.canWrite())
				return false;
			FileOutputStream fos = new FileOutputStream(fil);
			BufferedOutputStream bos=new BufferedOutputStream(fos);
			ObjectOutputStream oos=new ObjectOutputStream(bos);
			oos.writeObject(objeto);
			oos.close();
			bos.close();
			fos.close();
			return true;
		} catch (Exception exc) {
			K.escribir("- Error abriendo archivo \"" + archivo + "\" para guardar.\n");
			K.escribir(exc.getMessage() + "\n");
			exc.printStackTrace();
			return false;
		}
	}
	static Object obtenerObjeto(String archivo) { // deserializa un objeto y lo obtiene de un archivo
		try {
			Object objeto;
			File fil = new File(archivo);
			if (!fil.exists() || !fil.isFile() || !fil.canRead())
				return null;
			FileInputStream fis = new FileInputStream(fil);
			BufferedInputStream bis=new BufferedInputStream(fis);
			ObjectInputStream ois=new ObjectInputStream(bis);
			objeto=ois.readObject();
			ois.close();
			bis.close();
			fis.close();
			return objeto;
		} catch (Exception exc) {
			K.escribir("- Error abriendo archivo \"" + archivo + "\" para guardar.\n");
			K.escribir(exc.getMessage() + "\n");
			exc.printStackTrace();
			return null;
		}
	}

// ------------------------------------- main --------------------------------------------------------

	//* test	(comenzar línea con //* para activar, /* para desactivar ------------------------------------------
	public static void main(String[] args) {
		try {
			ejecutarPrograma();
		} catch (Exception exc) {
			escribir("\n- Ejecución con error. Abortando programa.\n" + exc);
			System.exit(1);
		}
		escribir("\n- Programa terminado normalmente.\n");
		System.exit(0);
		return;
	}

	static void ejecutarPrograma() {
		limpiarConsola();
		escribir("Listado del directorio actual:\n");
		escribir("------------------------------\n");
		for (String nombre : listarDirectorio("Cannonball/src/"))
			escribir(nombre + " [" + getPropiedadesArchivo("Cannonball/src/"+nombre) + "]\n");
		return;
// necesario para conmutar main	*/
	}
}
