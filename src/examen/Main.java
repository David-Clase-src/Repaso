package examen;

import java.util.ArrayList;
import java.util.List;

public class Main {	
	public static void main(String[] args) {
		System.out.println("Ejercicio A:");
		Ejercicios.ejercicioA();
		System.out.println();
		
		System.out.println("Ejercicio B:");
		ArrayList<Pais> paises = Ejercicios.ejercicioB();
		for(Pais pais : paises) {
			System.out.println(pais);
		}
		System.out.println();
		
		System.out.println("Ejercicio C:");
		Ejercicios.ejercicioC();
		System.out.println();
	}	
}

