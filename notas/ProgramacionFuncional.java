import java.util.function.*;
// Este paquete contiene numerosas interfaces (que las llamamos funcionales)
// Nos permiten crear variables que apunten a funciones
// - Supplier<T> -> Funciones que no recibe parámetros, devuelve un valor de tipo T
//                  Cualquier getter, toUpperCase, etc
// - Consumer<T> -> Funciones que reciben un parámetro de tipo T, no devuelven nada
//                  Cualquier setter, println, etc
// - Function<T,R> -> Funciones que reciben un parámetro de tipo T, devuelven un valor de tipo R
//                  Cualquier función que transforme un valor en otro
// - Predicate<T> -> Funciones que reciben un parámetro de tipo T, devuelven un valor booleano
//                   Funciones tipo: test(), isXXX(), hasXXX()
// Luego lo que tenemos son 40 variantes de estas interfaces que permiten más parámetros, tipos primitivos, etc
// BiFunction<T,U,R> -> Recibe dos parámetros (T,U), devuelve R
// BiConsumer<T,U> -> Recibe dos parámetros (T,U), no devuelve nada

// Para ejecutar las funciones a las que apunta una variable de los tipos anteriores, esas interfaces me dan funciones especiales:
// - Supplier<T> -> método get()
// - Consumer<T> -> método accept(T t)
// - Function<T,R> -> método apply(T t)
// - Predicate<T> -> método test(T t)

import java.util.List;

public class ProgramacionFuncional {
 
    public static String generarSaludoFormal(String nombre){
        return "Buenos días, Sr./Sra. " + nombre;
    }
    
    public static String generarSaludoInformal(String nombre){
        return "Hola " + nombre + "!";
    }

    public static void imprimirSaludo(Function<String,String> funcionGeneradoraDeSaludos, String nombre){
        String saludo = funcionGeneradoraDeSaludos.apply(nombre);
        System.out.println(saludo);
    }

    public static void main(String[] args) {

        Function<String,String> variable = ProgramacionFuncional::generarSaludoFormal; // Cómo referenciamos una función? Con un operador nuevo que sale en Java 8: '::'
        String saludo = variable.apply("Menchu"); // Ejecuta la función a la que apunta la variable con el valor Menchu
        System.out.println(saludo);

        imprimirSaludo(ProgramacionFuncional::generarSaludoFormal, "Menchu");
        imprimirSaludo(ProgramacionFuncional::generarSaludoInformal, "Menchu");
        // Imaginad que no quiero re-usar generarSaludoFormal. Pregunta.. 
        // Me está aportando legibilidad tener esa función en otro sitio de mi código definida con una sintaxis tradicional?

        // Entonces JAVA 8, me ofrece una nueva sintaxis para definir funciones: EXPRESIONES LAMBDA, que se introducen gracias al nuevo operador FLECHA de JAVA 8: '->'
        // Qué es una expresión lambda? Es un trozo de código que devuelve una función anónima creada/definida dentro de la propia expresión.
        // Que es una expresión en un código de programación? Trozo de código que produce un valor

        System.out.println("----- LAMBDA -----"); // Statement (Sentencia = FRASE , ORACION en JAVA)
        int numero = 5 + 7; // Otro statement
                     ///// ESPRESION: Trozo de código que produce un valor
                     /// 
        Function<String,String> variable2 = (String nombre) -> { // DEFINIR UNA FUNCION, con una sintaxis alternativa a la tradicional.
                                                                    return "Buenos días, Sr./Sra. " + nombre; // El tipo de vuelta se infiere... de esta linea
                                                                };

        imprimirSaludo((String nombre) -> { return "Buenos días, Sr./Sra. " + nombre; }, "Menchu");
        imprimirSaludo((nombre) -> { return "Buenos días, Sr./Sra. " + nombre; }, "Menchu"); // El tipo de los argumentos también se infiere.
        imprimirSaludo(nombre -> { return "Buenos días, Sr./Sra. " + nombre; }, "Menchu");
        imprimirSaludo(nombre -> "Buenos días, Sr./Sra. " + nombre, "Menchu");

        //Eso son las lambdas... una forma alternativa de definir funciones anónimas "al vuelo"

        // Para que leches vale esto. Ejemplos prácticos:
        List<String> nombres = List.of("Ana", "Pedro", "Juan", "Maria");
        // Y quiero iterarlo... Pre Java 1.5
        for (int i = 0; i < nombres.size(); i++) {
            System.out.println(nombres.get(i));
        }
        // En Java 5 , aparecen los for-each
        for (String nombreIterado : nombres) {
            System.out.println(nombreIterado);
        }
        // Desde java 8: 
        // Todas las colecciones tienen un método llamado forEach que recibe un Consumer<T> (una función que recibe un parámetro y no devuelve nada)
        nombres.forEach( System.out::println ); // Esto no es solo más compacto.. Es mucho más eficiente a la hora de ejecutar el código.
    }
}