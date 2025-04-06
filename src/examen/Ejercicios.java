package examen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.ISerializer;
import com.davicro.core.serialization.serializers.DataSerializer;
import com.davicro.core.serialization.serializers.StringSerializer;

public class Ejercicios {
	
	public static void ejercicioA() {
		File inFile = new File("paises.txt");
		File outFile = new File("paisesConFormato.txt");
		
		//Primero intentar crear el fichero pais si no existe (no sobreescribe los datos si existen)
		try {
			UtilidadesPaises.crearFicheroPaises(inFile);
		} catch (IOException e) {
			System.err.println("No se ha podido crear el archivo paises");
			return;
		}
		
		try(BufferedReader reader = new BufferedReader(new FileReader(inFile));
				PrintWriter writer = new PrintWriter(outFile)){
			
			//El nombre del pais se lee al principio de cada iteracion
			String pais;
			while((pais = reader.readLine()) != null) {
				String poblacion = reader.readLine();
				String extension = reader.readLine();
				String pib = reader.readLine();
				
				if(poblacion == null || extension == null || pib == null) {
					System.err.println("No hay suficientes datos en el archivo");
					break;
				}
				
				String formatted = UtilidadesPaises.FORMATO_PAIS.formatted(pais, poblacion, extension, pib);
				writer.println(formatted);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Archivo creado.");
	}
	
	public static ArrayList<Pais> ejercicioB() {
		ISerializer<ArrayList<Pais>> serializer = new StringSerializer<ArrayList<Pais>>(
				UtilidadesPaises::guardarPaisesString, 
				UtilidadesPaises::leerPaisesString);
		
		try {
			ArrayList<Pais> paises = serializer.load(UtilidadesPaises.FICHERO_PAISES_FORMATEADO);
			return paises;
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Pais>();
		} catch (DeserializationException e) {
			e.printStackTrace();
			return new ArrayList<Pais>();
		}
	}
	
	public static void ejercicioC() {
		ISerializer<ArrayList<Pais>> serializer = new DataSerializer<ArrayList<Pais>>(
				UtilidadesPaises::guardarPaisesBinario, 
				UtilidadesPaises::leerPaisesBinario
		);
		ArrayList<Pais> paises = ejercicioB();
		try {
			serializer.save(UtilidadesPaises.FICHERO_PAISES_BINARIO, paises);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
