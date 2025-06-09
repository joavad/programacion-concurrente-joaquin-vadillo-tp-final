package model;

// Importa clases necesarias para el framework Fork/Join
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

// Clase que implementa QuickSort concurrente usando Fork/Join
public class QuickSortConcurrente extends RecursiveAction {
    // ID de versión de clase serializable (buena práctica)
    private static final long serialVersionUID = 1L;

    // Umbral a partir del cual se hace recursión paralela, debajo de eso se usa el secuencial
    private static final int UMBRAL_DIRECTO = 10_000;

    // Arreglo que se va a ordenar
    private final int[] array;

    // Índice inicial del segmento a ordenar
    private final int inicio;

    // Índice final del segmento a ordenar
    private final int fin;

    // Constructor que inicializa los campos necesarios para la tarea
    public QuickSortConcurrente(int[] array, int inicio, int fin) {
        this.array = array;
        this.inicio = inicio;
        this.fin = fin;
    }

    // Método que define el trabajo que realiza esta tarea (Fork/Join)
    @Override
    protected void compute() {
        // Si el segmento es válido (al menos dos elementos)
        if (inicio < fin) {
            // Si el segmento es chico, usamos el algoritmo secuencial
            if ((fin - inicio) < UMBRAL_DIRECTO) {
                QuickSortSecuencial.quickSort(array, inicio, fin);
            } else {
                // En caso contrario, particionamos el arreglo y dividimos la tarea en dos
                int pivote = dividir(array, inicio, fin);

                // Creamos subtareas para la parte izquierda y derecha
                QuickSortConcurrente izquierda = new QuickSortConcurrente(array, inicio, pivote - 1);
                QuickSortConcurrente derecha = new QuickSortConcurrente(array, pivote + 1, fin);

                // Ejecutamos ambas subtareas en paralelo
                invokeAll(izquierda, derecha);
            }
        }
    }

    // Método que realiza la partición del arreglo y devuelve el índice del pivote
    private int dividir(int[] arr, int low, int high) {
        // Usamos el último elemento como pivote
        int pivote = arr[high];

        // Inicializamos el índice del menor
        int i = low - 1;

        // Recorremos desde low hasta high - 1
        for (int j = low; j < high; j++) {
            // Si el elemento actual es menor o igual al pivote
            if (arr[j] <= pivote) {
                i++; // Avanzamos el índice del menor
                intercambiar(arr, i, j); // Intercambiamos elementos
            }
        }

        // Colocamos el pivote en su posición final
        intercambiar(arr, i + 1, high);
        return i + 1; // Devolvemos el índice del pivote
    }

    // Método auxiliar para intercambiar dos elementos del arreglo
    private void intercambiar(int[] arr, int i, int j) {
        int temp = arr[i]; // Guardamos el valor original
        arr[i] = arr[j];   // Asignamos el valor del segundo al primero
        arr[j] = temp;     // Asignamos el valor guardado al segundo
    }

    // Método público para ejecutar el algoritmo QuickSort concurrente con ForkJoin
    public static void ordenarConForkJoin(int[] arreglo, int inicio, int fin, int hilos) {
        // Creamos un ForkJoinPool con la cantidad de hilos indicada
        ForkJoinPool pool = new ForkJoinPool(hilos);

        // Creamos la tarea principal para el arreglo completo
        QuickSortConcurrente tareaRaiz = new QuickSortConcurrente(arreglo, inicio, fin);

        // Ejecutamos la tarea principal de forma concurrente
        pool.invoke(tareaRaiz);

        // Cerramos el pool cuando termina el trabajo
        pool.shutdown();
    }
}