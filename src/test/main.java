package test; 

import model.QuickSortConcurrente; 
import model.QuickSortSecuencial;  

import java.util.Arrays;           
import java.util.Random;           
import java.util.concurrent.ExecutionException; 

public class main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Tamaños de arreglos que se van a probar
        int[] tamanios = {1_000, 10_000, 100_000, 1_000_000, 3_000_000,5_000_000};

        // Obtiene la cantidad de núcleos disponibles en el sistema
        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Hilos disponibles: " + numThreads); // <--- Agregá esta línea

        // Crea una instancia de Random para llenar los arreglos con datos aleatorios
        Random rand = new Random();

        // Itera sobre cada tamaño de arreglo
        for (int size : tamanios) {
            System.out.println("----- Tamaño del arreglo: " + size + " -----");

            // Crea y llena el arreglo para la versión secuencial
            int[] arrSecuencial = new int[size];
            for (int i = 0; i < size; i++) {
                arrSecuencial[i] = rand.nextInt(); // Llena con números aleatorios
            }

            // Copia el arreglo para usarlo en la versión concurrente
            int[] arrConcurrente = Arrays.copyOf(arrSecuencial, size);

            // Mide el tiempo de ejecución del QuickSort secuencial
            long startSeq = System.nanoTime();
            QuickSortSecuencial.quickSort(arrSecuencial, 0, size - 1);
            long endSeq = System.nanoTime();

            // Mide el tiempo de ejecución del QuickSort concurrente
            long startConc = System.nanoTime();
            QuickSortConcurrente.ordenarConForkJoin(arrConcurrente, 0, size - 1, numThreads);
            long endConc = System.nanoTime();

  

            // Imprime el tiempo que tardó la versión secuencial
            System.out.printf("Tiempo secuencial: %.6f segundos\n", (endSeq - startSeq) / 1e9);
            // Imprime el tiempo que tardó la versión concurrente
            System.out.printf("Tiempo concurrente: %.6f segundos\n", (endConc - startConc) / 1e9);

           
            System.out.println();
        }
    }}