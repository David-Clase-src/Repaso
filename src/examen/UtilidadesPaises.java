package examen;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UtilidadesPaises {
	public static final String FICHERO_PAISES = "paises.txt";
	public static final String FICHERO_PAISES_FORMATEADO = "paisesConFormato.txt";
	public static final String FICHERO_PAISES_BINARIO = "paises.dat";
	public static final String FORMATO_PAIS = "%s;%s*%s;%s";

	/**
	 * Crea un nuevo fichero con los datos necesarios si no existe
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void crearFicheroPaises(File file) throws IOException {
		if (file.exists())
			return;

		file.createNewFile();

		try (PrintWriter writer = new PrintWriter(file)) {
			writer.write("Brasil\r\n" + "212.6\r\n" + "8516000\r\n" + "14550.54\r\n" + "Francia\r\n" + "68.39\r\n"
					+ "543940\r\n" + "38625.07");
		}
	}

	/**
	 * Guarda una lista de paises a un archivo de texto usando un
	 * <code>PrintWriter</code>
	 * 
	 * @param paises
	 * @param writer
	 */
	public static void guardarPaisesString(ArrayList<Pais> paises, PrintWriter writer) {
		for (Pais pais : paises) {
			writer.println(FORMATO_PAIS.formatted(pais.nombre(), pais.poblacion(), pais.extension(), pais.pib()));
		}
	}

	/**
	 * Convertir un fichero de texto a una lista de paises usando un
	 * <code>BufferedReader</code>
	 * 
	 * @param reader
	 * @return
	 */
	public static ArrayList<Pais> leerPaisesString(BufferedReader reader) {
		ArrayList<Pais> paises = new ArrayList<Pais>();
		try {
			String linea;
			while ((linea = reader.readLine()) != null) {
				Pais pais = convertirPais(linea);
				if (pais != null && cumpleRequisitosTexto(pais))
					paises.add(pais);
			}
		} catch (IOException e) {
			// Comemos el error y devolvemos lo que tengamos
			e.printStackTrace();
		}

		return paises;
	}

	public static ArrayList<Pais> leerPaisesStringSinFormato(BufferedReader reader) throws IOException {
		ArrayList<Pais> paises = new ArrayList<Pais>();

		String nombre;
		while ((nombre = reader.readLine()) != null) {
			String poblacion = reader.readLine();
			String extension = reader.readLine();
			String pib = reader.readLine();

			if (poblacion == null || extension == null || pib == null) {
				System.err.println("No hay suficientes datos en el archivo");
				break;
			}
			
			try {
				Pais pais = convertirPais(nombre, poblacion, extension, pib);
				paises.add(pais);
			} catch(NumberFormatException e) {
				System.err.println("Error al convertir del fichero a pais");
			}
		}

		return paises;
	}
	
	/**
	 * Guardar una lista de paises en formato binario (solo nombre y poblacion),
	 * usando un <code>DataOutput</code>
	 * 
	 * @param paises
	 * @param output
	 * @throws IOException
	 */
	public static void guardarPaisesBinario(ArrayList<Pais> paises, DataOutput output) throws IOException {
		for (Pais pais : paises) {
			if (cumpleRequisitosBinario(pais)) {
				output.writeUTF(pais.nombre());
				output.writeDouble(pais.poblacion());
			}
		}
	}

	/**
	 * Convertir un fichero binario a una lista de paises (solo nombre y poblacion),
	 * usando un <code>DataInput</code>
	 * 
	 * @param input
	 * @return
	 */
	public static ArrayList<Pais> leerPaisesBinario(DataInput input) {
		ArrayList<Pais> paises = new ArrayList<Pais>();
		while (true) {
			try {
				Pais pais = new Pais(input.readUTF(), input.readDouble(), 0, 0);
				paises.add(pais);
			} catch (EOFException e) { // Final del fichero
				break;
			} catch (IOException e) {
				// Comemos el error y devolvemos lo que tengamos
				e.printStackTrace();
			}
		}

		return paises;
	}

	/**
	 * Convertir una linea de texto formateada en un objeto Pais
	 * 
	 * @param linea
	 * @return null si no se ha podido convertir
	 */
	public static Pais convertirPais(String linea) {
		String[] partes = linea.split("[;*]");

		if (partes.length < 4) {
			System.err.println("La linea %s no tiene los suficientes datos para convertir a pais".formatted(linea));
			return null;
		}

		try {
			String nombre = partes[0];
			double poblacion = Double.parseDouble(partes[1]);
			int extension = Integer.parseInt(partes[2]);
			double pib = Double.parseDouble(partes[3]);
			return new Pais(nombre, poblacion, extension, pib);
		} catch (NumberFormatException e) {
			System.err.println("Error de formato de numero al convertir %s a pais.".formatted(linea));
			return null;
		}
	}

	public static Pais convertirPais(String nombre, String poblacion, String extension, String pib) {
		return new Pais(nombre, Double.parseDouble(poblacion), Integer.parseInt(extension), 
				Double.parseDouble(pib));

	}

	private static boolean cumpleRequisitosTexto(Pais pais) {
		return pais.extension() > 500000 && (pais.poblacion() >= 30 && pais.poblacion() <= 250);
	}

	private static boolean cumpleRequisitosBinario(Pais pais) {
		return pais.extension() > 600000;
	}
}
