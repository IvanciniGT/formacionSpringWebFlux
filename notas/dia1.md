
# Dia 1:

- Ponernos a nivel. Programación funcional
                    Spring, Springboot... y que leches son esas cosas.
                
# Día 2:

- Montar un servicio REST con Springboot y SpringWeb , siguiendo las mejores practicas a día de hoy

# Día 3:

- Seguiremos con lo de antes.
- Introducir Programación reactiva, programación asíncrona, callbacks... 
- Introducir WebFlux

# Dia 4, 5, 6

Trabajaremos harto con WebFlux: Mono, Flux, y demás.

Mono y Flux no son cosas (clases) que ofrece SpringWebFlux.. Son clases del proyecto Reactor, que es un proyecto de Pivotal (la empresa detrás de Spring) para ofrecer programación reactiva en Java.

---

# Paradigmas de programación

Son solo un nombre bastante hortera por cierto, que los desarrolladores ponemos a las distintas formas de usar un lenguaje de programación.
Pero.. esto no es solo de lenguajes de programación! En los lenguajes naturales, los que hablamos todos los días, también hay distintos paradigmas... aunque no les llamamos por ese nombre "hortera".

> Felipe, IF (Si ) hay algo que no sea una silla: Condicionales
    > LO QUITAS  (más imperativo )
> Felipe, if not silla debajo de la ventana: Condicionales
    > If silla == False then GOTO IKEA ! 
        >Compras silla
    > Felipe, pon una silla debajo de la ventana.           IMPERATIVO

Nos asquea el lenguaje imperativo.. casa dia más. Estamos muy acostumbrados a él.. y por ello no nos damos cuenta de que es "UN ASCAZO".
INDEFENSIÓN APRENDIDA (psicología).. esto es lo. que nos pasa. 

Los lenguajes imperativos nos hacen olvidar nuestro objetivo: "Tener una puñetera silla debajo de la ventana" 
He pasado a centrarme en cómo conseguir ese objetivo.. y no en el objetivo en sí.

> Felipe, debajo de la ventana tiene que haber una silla. Es tu  responsabilidad! DECLARATIVO

No le doy una orden. Solo le explico cómo tienen que ser las cosas.. cómo son!
Le asigno/delego la responsabilidad de conseguir el estado que a mi me interesa a Felipe.

JAVA SOPORTA PARADIGMA DECLARATIVO? Si claro.. LAS ANOTACIONES son paradigma declarativo.

- Imperativo:           cuando le damos a la computadora una secuencia de instrucciones a ejecutar que debe procesar secuencialmente.
                        En ocasiones queremos romper esa secuencialidad.. y nos salen las típicas expresiones de control de flujo: if, for, while, switch...
- Procedural:           Cuando el lenguaje me permite agrupar muchas de estas ordenes en un conjunto, al que le pongo un nombre; y posteriormente
                        invocar/ solicitar la ejecución de ese conjunto de instrucciones por su nombre, decimos que el lenguaje soporta el paradigma procedural.
                        Funciones, procedimientos, métodos, subrutinas...
                        Por qué hacemos esto? Qué aporta?
                        - Reutilizar código
                        - Mejorar la legibilidad del código >>> Facilitar el mantenimiento futuro del código
- Orientado a objetos:  Todo programa maneja datos. Y esos datos tienen un tipo. Y todo lenguaje vienen con una serie de tipos de datos predefinidos.
                        Cuando el lenguaje me permite definir mis propios tipos de datos, con sus características y comportamientos asociados, decimos que el lenguaje soporta el paradigma orientado a objetos.

                                TIPO DE DATOS   Caracteriza por.              Operativas
                                String          una secuencia de caracteres   .toUpperCase(), .substring(), .charAt()
                                LocalDate       día, mes, año                 .plusDays(), .minusMonths(), .getYear()
                                Perrito         raza, edad, color, nombre     .ladrar(), .comer(), .dormir()
                                RepositorioDeMascotas                          guardarMascota(), borrarMascota(), buscarPorNombre()

                        Luego entran conceptos más avanzados: herencia, extensiones, polimorfismo, interfaces, sobrecarga, sobreescritura...
                        Entidad = tipo de clase que modela objeto del mundo real
- Declarativo:          Cuando el lenguaje me permite expresar lo qué quiero conseguir, sin necesidad de detallar el cómo conseguirlo.
                        decimos que el lenguaje soporta el paradigma declarativo.
                        En Java, especialmente con SpringBoot, usamos mucho las Anotaciones, que son una forma de programación declarativa.
- Funcional:            Cuando el lenguaje me permite que una variable apunte a una función, y posteriormente pueda invocar esa función a través de 
                        la variable, decimos que el lenguaje soporta el paradigma funcional.
                        El tema no es lo que es la programación funcional.. que es una chorrada.
                        Es lo que puedo hacer, una vez que el lenguaje soporta eso. Y aquí es donde nos vuela la cabeza:
                        - Puedo crear funciones que acepten funciones como parámetros de entrada
                        - Puedo crear funciones que devuelvan funciones como resultado (closures)
                        Y esto es complejo... porque no estamos acostumbrados.. Pero una vez que estamos acostumbrados, nos damos cuenta de que es una pasada.. y no quiero otra cosa. La POO para algunos escenarios es un tostón enorme!!! Muy verbosa.. demasiado... asquerosamente verbosa.
                        Y aveces quiero cosas más simples.
                        Y a veces necesito programación funcional.
                        Puedo vivir sin ella? CLARO... igual que puedo vivir sin POO.
                        Cuando el lenguaje me ofrece programación funcional, hay una tercera razón para crear funciones:
                        - Mejorar la legibilidad del código >>> Facilitar el mantenimiento futuro del código
                        - Reutilizar código
                        - Porque una función ME OBLIGA a pasarle otra función como argumento.




# Programación funcional para dummies

La programación funcional es un paradigma de programación.


---

# Programación asíncrona.

Estamos en el mundo del backend.
Y la programación backend es muy diferente a la programación frontend.
En el frontend, todo el código de nuestro programa queremos que sea asíncrono.
En el mundo del backend, en general no usamos programación asíncrona.

## Programación síncrona
cliente ----> Http request ----> Servidor
                                  Queue<HttpRequest> <  ThreadPool -> Hilo1.. y ese hilo se pone a procesar la request..., llama a la BBDD, espera la respuesta de la BBDD, llama un a un microservicio, espera la respuesta del microservicio, procesa la información, genera la HttpResponse y la envía de vuelta al cliente.
                                  Y todo eso lo hacemos en un hilo.
                                  El servidor web (tomcat, weblogic, jetty...) tiene un pool de hilos (ThreadPool) para procesar las peticiones que van llegando.
                                  De hecho es uno de los parámetros de configuración más importantes de un servidor web: tamaño del pool de hilos.
                                  A más hilos, más capacidad de atender peticiones en paralelo.
                                  Pero también mayor consumo de recursos (memoria, CPU...).
                                  Si nos centramos en la RAM, cada hilo ejecuta el programa.. y creará sus propios datos... en RAM.
                                  Más hilos, más ejecución en paralelo... más datos y pero más consumo de RAM.
                                  Pero los hilos en si mismos producen un overhead en RAM. Cuestan RAM.. solo la estructura del hilo.
                                  Y además, computacionalmente, el SO tiene que gestionar esos hilos... y eso también consume CPU.
                                  No es nada barato abrir un hilo.
                                  Y en un servidor web, Podemos abrir .. 100-200 hilos... pero no 1000... Necesito un tera de ram para eso.
                                  Por lo tanto estamos limitados en el número de hilos que podemos abrir en un servidor web, de cara a poder ejecutar peticiones HTTP en paralelo.

## Programación asíncrona!

Tiene que ver con un mecanismo de comunicación entre programas.
Llamo a algo.. y espero su respuesta (bloqueo la ejecución del hilo que está ejecutando mi programa hasta que obtiene respuesta)            # SINCRONO
- Una llamada de teléfono es síncrona. Llamo y espero a que me respondan para poder entregar mi mensaje.
- Un whatsapp es asíncrono. Mando el mensaje y sigo con mi vida. Cuando la otra persona responde, recibo su respuesta.. y tomo acción ante su respuesta.
  Pero no espero... sigo haciendo cosas.

# Cómo se programa esto en JAVA?

En Java, tenemos los Future<T> y CompletableFuture<T> para programar de forma asíncrona.
Y puedo crear funciones que devuelvan un Future<T> o un CompletableFuture<T>.
```java
public Camisa llevarCamisaATintoreriaSincrono(Camisa camisa){
} // Puedo esperar en la puerta de la tintoreria a que acaben... y mientras yo de brazos cruzados esperando.. no hago nada más.

public Future<Camisa> llevarCamisaAtintoreriaAsincrono(Camisa camisa){
} // Me pueden dar un ticket.. un VALE POR UNA CAMISA
```
Lo que me entregan es un ticket (FUTURE, PROMISE).
Cuando quiera puedo reclamar esa camisa, presentando el ticket.

Que tenga el ticket implica que me darán la camisa? O NO.... quizás se quema la camisa, la tintoreía y me quedo sin camisa.
Hay un  Exception al lavar la camisa. y me quedo sin ella... Es lo que hay. Me dan una promesa (PROMISE )... algo que quizás ocurra en el futuro (FUTURE).
Ya veremos...

```java

Future<Camisa> ticket = llevarCamisaAtintoreriaAsincrono(miCamisa);
// Aqui podría seguir haciendo cosas... mientras la tintorería lava mi camisa.
Camisa miCamisa = ticket.get(); // Aquí BLOQUEO el hilo que está ejecutando mi programa hasta que la tintorería me entregue la camisa.
                                // Si llego a la puerta y aún mi camisa no está lista, me quedo esperando como un pendejo... Bueno.. al menos algo de tiempo he ganado para hacer otras cosas.
```

Esta no es la única forma de crear funciones asíncronas. Podemos usar funciones de callback.
```java
public void llevarCamisaAtintoreriaAsincrono(Camisa camisa, Callback<Camisa> códigoQueDebeEjecutarseCuandoLaCamisaEsteLista){
} // Me pueden dar un ticket.. un VALE POR UNA CAM


llevarCamisaATintoreriaAsincrono(miCamisa, camisaLista -> guardarCamisaEnArmario(camisaLista) );
// Y ahora sigo con mi vida.
// Ya dejé configurado lo que quiero que ocurra cuando la camisa esté lista.
// Ya no es problema mio.. Ya no espero a nadie... sigo con mi vida.
// Cuando la camisa esté lista, se ejecutará el código que he pasado como callback.... lo hará el hilo que esté gestionando la tintorería. No yo. Me he quitado el marrón de encima.
```

Esto es lo que llamamos programación reactiva.... que es una forma de conseguir programación asíncrona.

Esto es lo que nos da la librería REACTOR, que es la que usa internamente SpringWebFlux.. y que me ofrece las clases: Mono<T> y Flux<T>.. QUE YA VEREMOS QUE LECHES SON.
El punto por ahora es que esto de la programación funcional no es una elección en este caso. ES IMPERATIVO (y no me refiero al paradigma) que usemos programación asíncrona en el backend.

Lo hilos (concepto que está definido a nivel de SO, no de lenguaje de programación) son los encargados de ejecutar el código de nuestro programa.
Cada vez que creo un proceso en un SO, se abre por defecto un hilo para ejecutar el código de ese proceso MAIN.
Luego podemos crear más hilos... para empezara realizar tareas en paralelo.

---

> Me compro un coche nuevo. Al año lo llevo al taller para que le hagan la primera revisión.

Lo sabéis por que por definición un coche es un producto sujeto a mantenimientos.

Lo llevo.. voy un jueves a las 9:00. Y pregunto.. a qué hora me lo tendréis listo? y cuánta pasta me va a costar?
Respuesta del taller: UFF!!! eso es muy complejo... hay que desmontar el motor entero, para llegar al tapón de llenado del aceite... y luego hay que volver a montar todo el motor... y eso lleva su tiempo, es delicado... hay que hacer pruebas... Vente en un mes a por el coche...y prepara 7000€

> Pregunta: Como os sentiríais? ESTAFADOS

Por qué por definición un coche es un producto sujeto a mantenimientos. Y los ingenieros debían haber previsto que el usuario final tendría que hacerle mantenimientos al coche.
Y haber colocado las cosas de forma que fuera sencillo hacer esos mantenimientos. Estamos de acuerdo?

Para que me vendan un coche, es suficiente con que funcione? que ande? NO... eso se da por descontado.. Si no anda, on es un coche.. es un trasto de metal muy pesado y un tanto inútil.

Y porqué con el software es diferente?

Hay una frase que YO (IVAN) tatuaría a cualquier persona que se dedique a desarrollar software... en los nudillos:
"UN PRODUCTO DE SOFTWARE ES UN PRODUCTO SUJETO A CAMBIOS Y MANTENIMIENTOS... POR DEFINICION"


    Escribo código <> PRUEBAS -> OK ---> Refactorización <> PRUEBAS -> OK -> Entregar
    <-------50% del trabajo ------->.   <-------50% del trabajo ------->
              8 horas                               8 horas

Y no hacerlo:

1. Es no entender nada del mundo del desarrollo de software :   YA QUE POR DEFINICIÓN EL SOFTWARE ES UN PRODUCTO SUJETO A CAMBIOS Y MANTENIMIENTOS
2. Es estafar al cliente:                                       YA QUE POR DEFINICIÓN EL SOFTWARE ES UN PRODUCTO SUJETO A CAMBIOS Y MANTENIMIENTOS

---

El hecho es que cada vez somo más conscientes de la importancia de la calidad del código... y de poder crear un código que sea fácil de mantener y evolucionar en el futuro.
Esto toma especial relevancia cuando usamos metodologías ágiles, donde el software se va construyendo de forma incremental e iterativa.

# Cuál es la característica principal de una metodología ágil? 

Entregar el producto de forma incremental e iterativa.

En las metodologías tradicionales, se entregaba el producto final al cliente, cuando estaba completo.
Resultado: Falta de feedback del cliente durante el desarrollo, y producto final que no cumplía las expectativas del cliente.
Lo que redundaba en la necesidad de cambiar media aplicación tras la entrega, con el consiguiente coste y retraso.

Una de las cosas a las que hoy en día le damos una importancia enorme es a tener una buena arquitectura de software, que permita que el código sea fácil de mantener y evolucionar en el futuro.

Lo cual va en contraposición absoluta de las arquitecturas monolíticas de hace 10-15 años, que iban en paralelo con las metodologías tradicionales de desarrollo de software.

Hoy en día tiramos de arquitecturas de componentes desacoplados: Arquitecturas limpias, hexagonales, microservicios...

> Vamos a montar una app.

Quiero una app que por consola le pase 2 argumentos: IDIOMA y PALABRA
Y me diga si esa palabra existe en un diccionario que la app tenga de ese idioma... si es que tiene diccionario de ese idioma. Si existe que me ofrezca los significados de esa palabra en ese idioma.

$ buscarPalabra ES melón
La palabra MELÓN en español significa:
1. Fruto comestible de la planta Cucumis melo, de la familia de las cucurbitáceas.
2. Persona con pocas luces.

$ buscarPalabra ES archilococo
La palabra ARCHILOCOCO en español no existe en el diccionario de español.

$ buscarPalabra ELF melon
Lo siento, pero no tengo diccionario de ELF.

---

Tirando por lo bajo.. en modo cutre, esta app debería tener 6 repositorios de git.. y 6 proyectos maven.. como MUY poco.

> Pregunta...

 A día de hoy, las palabras (diccionarios) los vamos atener en un archivo de texto plano. Un archivo por idioma.
 Y tenemos una UI que nos ofrece información por consola.

 Si el sistema el día de mañana decidimos que los diccionarios los tenga en una BBDD, debería yo de tener que tocar la UI? NO
 Le importa a la UI el lugar del que se obtienen los datos? NO


---
1. Módulo / componente / Proyecto => .jar
El API de la gestión de los diccionarios (Interfaces)

2. Módulo / componente / Proyecto => .jar
Una implementación del gestor de diccionarios que obtiene los diccionarios de archivos de texto plano

3. Módulo / componente / Proyecto => .jar
El API de la UI (Interfaces)

4. Módulo / componente / Proyecto => .jar
Implementación de la UI por consola

5. Módulo / componente / Proyecto => .jar
Con la lógica de la app
    Lógica de la app puede ser de la forma:
    - Mensaje de bienvenida a la app
    - Obtengo los datos de Idioma y Palabra 
    - Intento recuperar un Diccionario para el idioma solicitado
        - Si no existe, muestro mensaje de error por UI y salgo
    - Intento buscar la palabra en el diccionario si es que tengo diccionario de ese idioma
        - Si no existe, muestro mensaje de error por UI y salgo
    - Si existe muestro los significados por UI
    - Doy las gracias, que soy buena gente!
    Y está necesitará para funcionar una UI y un gestor de diccionarios

6. Y los diccionarios donde los guardo? En cual de esos proyectos? En otro.
Acaso no puedo tener la versión 1.1 de la app trabajando con la versión 2 del diccionario de ES?
Y el día de mañana usar la misma versión de la app, pero usando la versión 3 del diccionario de ES?
Pues necesitaré otro repo de git, para controlar la versión.
---

# diccionarios-api.jar

```java

package diccionarios.api;

import java.util.List;
import java.util.Optional; // Desde Java 8

public interface Diccionario {
    String getIdioma();
    boolean existe(String palabra);
    Optional<List<String>> buscarPalabra(String palabra);
        // Si le pregunto por la palabra CASA en ES, devolverá una lista con los significados de la palabra CASA en ES .. Un List<String>
        // Y si no? una lista vacía.. o un null?
        // Lanzar un NoSuchWordException... pero sería una cagada descomunal. = MUY MALA PRACTICA
}

public interface SuministradorDeDiccionarios {
    boolean tieneDiccionarioDe(String idioma);
    Optional<Diccionario> obtenerDiccionario(String idioma);
}

```

---
Implementación del suministrador de diccionarios que obtiene los diccionarios de archivos de texto plano
# diccionarios-ficheros.jar

```java
package diccionarios.ficheros;

import diccionarios.api.Diccionario;
import diccionarios.api.SuministradorDeDiccionarios;

public class SuministradorDeDiccionariosFicheros implements SuministradorDeDiccionarios {
    
    public  SuministradorDeDiccionariosFicheros(String rutaDirectorioDiccionarios){
        // Aquí guardo la ruta del directorio donde están los archivos de texto plano con los diccionarios
    }

    // Implementación concreta que obtiene los diccionarios de archivos de texto plano
}

public class DiccionarioFichero implements Diccionario {
    // Implementación concreta de un diccionario que obtiene las palabras y sus significados de un archivo de texto plano
}
```

v1 del api, que admite significados
y tengo v1 de la implementación que obtiene los diccionarios de archivos de texto plano.
Y luego saco la v2 del api, que admite significados y sinónimos.
Y luego saco la v2 de la implementación que obtiene los diccionarios de bbdd

Y la de ficheros sigue con v1... todavia no admite sinónimos... y está bien.. voy paso a paso.. no me da tiempo a acabar el proyecto de golpe.

---

# Lógica de la app

```java

package app.logica;

import diccionarios.api.Diccionario;
import diccionarios.api.SuministradorDeDiccionarios;
//import diccionarios.ficheros.SuministradorDeDiccionariosFicheros; // Y con esta linea el proyecto murió! 
                                                                  // Más barato me sale tirarlo a la basura!
                                                                  // Aquí acabamos con esta linea de cagarnos en el principio de inversión de dependencias
                                                                  // Uno de los 5 grandes principios de desarrollo SOLID.

public class LogicaApp { // Imaginar que no tengo la UI como proyecto aparte.. está aquí integrada.

    public void procesar(String idioma, String palabra, SuministradorDeDiccionariosFicheros miSuministrador){    // Inyección de dependencias
        //SuministradorDeDiccionariosFicheros miSuministrador = new SuministradorDeDiccionariosFicheros("ruta/del/directorio/de/diccionarios");
        if(miSuministrador.tieneDiccionarioDe(idioma)){
            Diccionario diccionario = miSuministrador.obtenerDiccionario(idioma).get();
            if(diccionario.existe(palabra)){
                List<String> significados = diccionario.buscarPalabra(palabra).get();
                System.out.println("La palabra " + palabra.toUpperCase() + " en " + idioma + " significa:");
                significados.forEach( significado -> System.out.println("- " + significado) );
            } else {
                System.out.println("La palabra " + palabra.toUpperCase() + " en " + idioma + " no existe en el diccionario de " + idioma + ".");
            }
        } else {
            System.out.println("Lo siento, pero no tengo diccionario de " + idioma + ".");
        }
    }
}
```

Estos temas están muy estudiados.
Por gente que sabe un huevo de estas cosas.

# SOLID

En ciencias exactas (matemáticas, física, química...) un principio es una ley o regla fundamental inmutable, aceptada y universal.
Nosotros no estamos en ciencias exactas.. como si estaríamos si estuvieramos en una disciplina como CIENCIAS DE LA COMPUTACIÓN.
Nosotros estamos en desarrollo de software.. que es una disciplina de ingeniería de software.
Y una ingeniería no es una ciencia exacta, buscamos resolver un problema práctico, con una serie de restricciones (coste, tiempo, recursos...).
Y hay muchas potenciales soluciones a un mismo problema.... intentaré elegir la más optima para mi caso concreto.

La palabra principio en este contexto no tiene el mismo significado que en ciencias exactas. No es una ley inmutable.
Es más bien un ideal, que puedo decidir respetar o no. Es una decisión mia.
Como ser humano, tengo mis principios, que guían mi toma de decisiones.

Los principios SOLID me ayudan a tomar decisiones... decisiones encaminadas a generar un código más limpio, más fácil de mantener y evolucionar en el futuro.
Eso es lo que me garanatizan los principios SOLID? Que si los cumplo, respeto, mi código será fácil de mantener y evolucionar en el futuro

Son un compendio de 5 principios que juntó el Tio Bob (Robert C. Martin) allá por 2000-2001.. Clean Code -> Clean Architecture.

- S: Single Responsibility Principle (SRP)   Principio de responsabilidad única
- O: Open/Closed Principle (OCP)             Principio de abierto/cerrado
- L: Liskov Substitution Principle (LSP)     Principio de sustitución de Liskov
- I: Interface Segregation Principle (ISP)   Principio de segregación de interfaces
- D: Dependency Inversion Principle (DIP)    Principio de inversión de dependencias

# Principio de inversión de dependencias (DIP)

Un componente de alto nivel no debe depender de implementaciones concretas de componentes de bajo nivel. Ambos deben depender de abstracciones (interfaces).

    app-logica.jar -> diccionarios-api.jar <- diccionarios-ficheros.jar
       |                                              ^
       +----------------------------------------------+ <<< Esta dependencia es la que destroza la mantenibilidad del código.


    app-logica.jar -> diccionarios-api.jar <- diccionarios-ficheros.jar


Lo que dice es que a una clase no le pueden llegar flechas en el diagrama de componentes.
DE UNA CLASE SOLO PUEDEN SALIR FLECHAS.
A las interfaces si les pueden llegar flechas.

Cómo resuelvo este problema?

Puedo aplicar algún patrón de diseño conocido, estudiado, validado, aceptado en la industria.

# Patrón de diseño:

Una forma de escribir mi código, que resuelve un problema concreto de diseño de software, en un contexto concreto, con una serie de restricciones concretas.

Por ejemplo, para resolver el problema de la inversión de dependencias, puedo usar el patrón de diseño: Inyección de dependencias (Dependency Injection)

## Inyección de dependencias (Dependency Injection)

Una clase NUNCA debe crear instancias de las clases de las que depende. En su lugar, le deben ser suministradas esas dependencias desde el exterior.

Esto está guay... pero eres un cachondo.. Lo único que has hecho es pasar el problema a otro tio...
Es decir quien llame a esta función tendrá que hacer el new SuministradorDeDiccionariosFicheros(...)... no? SI

Pero, eso es normal.. no es un problema.


> Soy un fabricante de bicicletas

BTWIN > Decathlon

Voy a fabricar el sistema de frenado? NO
Voy a fabricar las ruedas?  NO
Voy a fabricar el manillar? NO
Voy a fabricar el sillín? NO
Voy a fabricar el cuadro?  NO

Qué pinto al final? Yo diseño los componentes.. los especifico e INTEGRO los componentes. Ese es mi papel.

Cuando diseñe el modelo de bicicleta ROAD 500XLT-87/09... diré que esa bici debe llevar el sistema de frenos SHIMANO XT-2000? NO, POR QUE NO
Porque si lo hago, me ato a ese sistema de frenos.. y si SHIMANO deja de fabricarlo? qué hago con mi bici!

YO Decathlon, lo que haré es especificar cómo debe ser un sistema de frenos compatible con mi bici.
Cómo debe ser una rueda compatible con mi bici.


La bici al final, montará el sistema de frenos SHIMANO XT-2000. Claro.. tiene que llevar uno concreto... una implementación. NO UNA ESPECIFICACIÓN (UN API)
Yo podré cambiarlo por otro sistema de frenos si se me rompe ese y SHIMANO ya no lo fabrica. La idea es que pueda montar cualquier sistema de frenos que cumpla con la especificación que yo he definido.

En un coche, mi coche tiene una especificación de ruedas: 205/55 R16 91V
Ahora.. cuando lo compro, me dan una rueda concreta... no una especificación. MICHELIN PRIMACY 4 205/55 R16 91V
Pero el día de mañana, lo puedo cambiar por otra rueda que cumpla con la misma especificación: BRIDGESTONE TURANZA T005 205/55 R16 91V

Malo sería que no!

...

A lo que vamos:

Mis componentes no dependen de implementaciones concretas de otros componentes.
Mi producto final si que lo que hace es integrar implementaciones concretas de esos componentes.
El día de mañana puedo cambiar esas implementaciones concretas por otras, siempre que cumplan con la especificación (API) que yo he definido.

Al final.. arriba del todo.. tendré que trabajar con implementaciones concretas. ESTO ES JAVA...
Al final alguien, en algún sitio tendrá que hacer un new SuministradorDeDiccionariosFicheros(...)
ESO HAY QUE HACERLO.

Lo que intentaré es no hacerlo yo.

---

# Que es Spring

Spring es un framework de desarrollo de aplicaciones en Java que ofrece un contenedor de Inversión de control! (IoC)
Igual que lo tengo en C#, .netCore
Igual que lo tengo en TS: Angular
Igual que existe en casi cualquier lenguaje de programación moderno.

Eso es spring.

# Que es un contenedor de Inversión de control (IoC)? Qué es la inversión de control?

Inversión de control: Un patrón de desarrollo de software por el cuál dejo de escribir el flujo de mi programa y se lo delego al framework que me ofrece la inversión de control.


---

> ETL: Extract, Transform, Load (SCRIPT de carga de datos.. desde un origen. El típico programa que ejecuto a las 3am los jueves para sacar datos de una BBDD, transformarlos y cargarlos en otra BBDD)

Requisitos: --- Por cierto.. que hemos hecho aquí? DECLARATIVO. Importa el orden de los requisitos?
- Que los datos los mande a una BBDD Oracle.
- Ah.. y quiero que cuando acabe me mande un email con el informe de la carga.
- Ah.. que se me olvidaba.. quiero que al arrancar también me mande un email diciendo que ha arrancado el proceso ETL.
- Ah.. y quiero que se validen los datos de la persona.. su DNI.. a ver si es bueno
- Ah.. por cierto, los datos de la persona son nombre, apellidos, DNI, fecha de nacimiento, email, teléfono.
- Y ... quiero que si una persona no tiene un DNI válido, no la cargue.. y me lo indique en el informe final.
- Y.. ah.. si... esto era importante.. quiero que si la persona no tiene 18 años, no la cargue.. y me lo indique en el informe final.
- Quiero lea datos de personas de un fichero EXCEL


CODIGO: Flujo principal del programa ETL... que tipo de lenguaje he usado? Imperativo
------------------------------------------
1º Mandar el email de arranque
2º Leer el fichero EXCEL
2.1º Abro conexión a la BBDD
3º Por cada persona leída:
    3.1º Validar DNI
    3.2º Validar edad
    3.3º Si es válida, cargar en BBDD
    3.4º Si no es válida, anotar en informe final
4º Cerrar conexión a la BBDD
5º Cierro el fichero EXCEL
6º Mandar el email con el informe final


Spring va a montar el flujo de mi app. Yo monto el flujo de mi app cuando trabajo con Spring.
A Spring le digo que componentes quiero tener en mi app.
Sporing:
- Quiero un repositorio de bbdd
- Quiero tener el concepto de persona (ENTITY), con estos datos
- Quiero tener un servicio de validación de DNI
- Quiero tener un servicio de validación de edad
- Quiero tener un servicio de envío de emails
- Quiero tener un servicio de lectura de ficheros EXCEL
- Quiero tener un servicio de carga en BBDD
- Quiero tener...
- Quiero tener...
- Quiero tener...
Spring monta el flujo.


Cuántas lineas de código tiene un programa Spring? 1

```java

@SpringBootApplication
public class MiAplicacionETL {
    public static void main(String[] args) {
        SpringApplication.run(MiAplicacionETL.class, args); // Inversión de control. Spring, ejecuta tu mi programa
    }
}

Y Spring flipa?! Y me dice.. pues que ostias de programa? Si tiene una puñetera clase vacía!
Y entonces yo le digo (DECLARATIVAMENTE)
@SpringBootApplication = Mi aplicación tiene un montón de componentes.. en el paquete donde esta esta clase y en sus subpaquetes.

BUSCATE LA VIDA! SPRING, FELIPE o como te llames! No es mi problema ejecutar mi programa.. es tu problema.
Busca los componentes, los instancias, monta el flujo de mi programa y ejecútalo. Y lees ficheros de props.. y arrancas servidores web... y haces un montón de cosas más.

@Service
@RestController         Spring, te informo que esta clase es un servicio REST.. Tu sabrás!
@Repository
@Component
@Configuration
@GetMapping

Eso es todo LENGUAJE DECLARATIVO

Spring es quien monta el flujo... yo solo le digo qué componentes quiero tener en mi app.

Claro.. si Spring es quien va a crear las instancias de mis clases... Que se busque la vida para entregarle los parámetros que necesiten mis clases.
Ya no soy quien va a hacer los new SuministradorDeDiccionariosFicheros(...)... Eso es cosa de Spring.
Y si el día de mañana quito el SuministradorDeDiccionariosFicheros y pongo un SuministradorDeDiccionariosBBDD... Spring lo encontrará.. y ejecutará new SuministradorDeDiccionariosBBDD(...)... no yo. Yo no he escrito ese código.. y por ende, no tengo que mantenerlo ni que cambiarlo en el futuro!

Spring , gracias a la inversión de control, me ayuda a aplicar el patrón de diseño de inyección de dependencias, para poder respetar el principio de inversión de dependencias (DIP) de SOLID.

Es tal la importancia de ese principio, que frameworks como Spring nacen para ayudarme a respetarlo.