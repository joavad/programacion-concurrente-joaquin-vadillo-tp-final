package model; 

public class QuickSortSecuencial {

    // Método principal del algoritmo QuickSort secuencial
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Encuentra la posición del pivote
            int pi = partition(arr, low, high);

            // Ordena recursivamente las sublistas
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    // Divide el arreglo y devuelve la posición del pivote
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Elige el último elemento como pivote
        int i = low - 1;

        // Coloca los elementos menores al pivote a la izquierda
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        // Coloca el pivote en su posición final
        swap(arr, i + 1, high);
        return i + 1;
    }

    // Intercambia dos elementos del arreglo
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}