package com.curso;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.net.URI.create;

@RestController
@RequestMapping("/api/v1")
public class ControladorRest {

    @GetMapping("/hello") // NO ES REACTIVO. Es bloqueante a no poder más... Modelo tradicional!
    public String hello() {
        return "Hello, World!";
    }
    @GetMapping("/bacon") // NO ES REACTIVO. Es bloqueante a no poder más... Modelo tradicional!
    public String bacon() {
        long tin = System.nanoTime();
        String aDevolver = pedirBaconIpsum();
        long tfin = System.nanoTime();
        System.out.println("Tiempo de respuesta de baconipsum: " + (tfin - tin)/1_000_000 + " ms");
        return aDevolver;
    }

    @GetMapping("/baconNoBloqueante") // NO ES REACTIVO. Es bloqueante a no poder más... Modelo tradicional!
    public Mono<String> baconNoBloqueante() {
        long tin = System.nanoTime();
        Mono<String> aDevolver = pedirBaconIpsumNoBloqueante();
        long tfin = System.nanoTime();
        System.out.println("Tiempo de respuesta de baconipsum: " + (tfin - tin)/1_000_000 + " ms");
        return aDevolver;
    }

    @GetMapping("/quieroMuchoBacon") // REACTIVO. NO bloqueante... Modelo WebFlux!
    public Flux<String> quieroMuchoBacon() {
        Flux<String> paraDevolver = Flux.range(1, 2)
                .flatMap( numero -> pedirBaconIpsumNoBloqueante() )
                ; // ACABADO!

        System.out.println("ESTOY AQUI !!!!!, Ya con el Flux!");
        return  paraDevolver;
        // Después de ejecutar el "pedirBaconIpsumNoBloqueante", y recibir aquí el flux,
        // lo que estamos haciendo es decir que por cada número del rango del 1 al 50,
        // se haga una llamada a pedirBaconIpsumNoBloqueante, y se emita el resultado dentro del flux.
        // Pero mi hilo no se queda aquí esperando a que se emitan los valores dentro del flux.
        // Mi hilo queda libre para atender otras peticiones.
        // Y a medida que las llamadas a pedirBaconIpsumNoBloqueante van devolviendo sus resultados,
        // esos resultados se van emitiendo dentro del flux.
        // Pregunta... A quién le estamos desde aquí devolviendo un Flux? Al curl? NO... A netty
        // Y netty, hace su propio trabajo, dejando programado que cuando los valores se emitan dentro del flux,
        // los envíe como respuesta HTTP al cliente que hizo la petición (los valores... lo de dentro... los String)
    }


    @GetMapping("/helloNoBloqueante") // REACTIVO. NO bloqueante... Modelo WebFlux!
    public Mono<String> helloNoBloqueante() {
        Mono<String> paraDevolver = generameUnTextoQueTardesMuchoEsperando()
                .map( texto -> texto.toUpperCase() )
                ; // ACABADO!

        System.out.println("ESTOY AQUI !!!!!, Ya con el Mono!");
        return  paraDevolver;
        // Después de ejecutar el "generameUnTextoQueTardesMuchoEsperando", y recibir aquí el mono,
        // lo que estamos haciendo es decir que cuando sea que sea emitido el valor dentro del mono,
        // lo transforme a mayúsculas.
        // Pero mi hilo no se queda aquí esperando a que se emita el valor dentro del mono.
        // Mi hilo queda libre para atender otras peticiones.
        // Y a los 5 segundos, cuando el valor se emite dentro del mono, mi hilo ( u otro hilo disponible del pool de hilos )
        // ejecuta la función que transforma el texto a mayúsculas.
        // Pregunta... A quién le estamos desde aquí devolviendo un Mono? Al curl? NO... A netty
        // Y netty, hace su propio trabajo, dejando programado que cuando el valor se emita dentro del mono,
        // lo envíe como respuesta HTTP al cliente que hizo la petición (el valor... lo de dentro... el String)
    }


    private Mono<String> generameUnTextoQueTardesMuchoEsperando(){
        //return Mono.just("Hello, World!");
        // Pregunta... esto que hemos hecho tiene algún sentido? NINGUNO... Es ridículo!
        // Por qué? No hay dentro de este código nada que sea bloqueante.. que requiera de un wait!... que me deje el hilo esperando sin hacer nada!
        // Para jugar, y probar estas cosillas, La Librería Project Reactor, me ofrece una función de coña... solo sirve para pruebas!
        return Mono.just(mensaje()).delayElement(Duration.ofSeconds(5));
        // Pregunta. Esta función responde inmediatamente o tarda 5 segundos en responder?
        // El mono se devuelve inmediatamente, o se devuelve en 5 segundos?
        // - El mono, se devuelve inmediatamente.
        // - El valor contenido dentro del mono, se emite (se "resuelve") dentro de 5 segundos.
        // el punto es que el hilo que atiende la petición HTTP, no se queda bloqueado esperando 5 segundos.
        // Esta función devuelve inmediatamente un Mono... y el hilo queda libre para atender otras peticiones.
        // Y en ese momento, continúa el hilo ejecutando lo que tenía pendiente.

        // Esto de arriba no es problema ninguno... Lo de añadir una llamada a mensaje() dentro de un Mono.just() no es bloqueante.
        // Pero no es bloqueante porque dentro de mensaje() no hay nada bloqueante.

        //return Mono.just(mensajeCabron());
        // En cambio este de arriba SI ES MAL ASUNTO! Acabo de paralizar el hilo que atiende la petición HTTP durante 5 segundos!
        // Eso impide que el hilo quede libre para atender otras peticiones.
        // La función de abajo tarda 5 segundos en devolver el String... y mientras tanto el hilo que atiende la petición HTTP está bloqueado esperando.
    }

    // Acabado! Esto es webflux. NO HAY NADA MAS!... NO ES COÑA ! YA ESTA ! Que no hay mas !


    private String mensaje(){
        return "Hola Mundo!";
    }


    private String mensajeCabron() {
        try {
            Thread.sleep(5000); // Simulo una operación bloqueante que tarda 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hola Mundo!";
    }

    // Creamos una función para pedir bacon! baconipsum! LorenIpsum
    // https://baconipsum.com/api/?type=all-meat&paras=50&start-with-lorem=1
    private String pedirBaconIpsum() {
        // llamada http con el cliente webclient de java
        String url = "https://baconipsum.com/api/?type=all-meat&paras=1&start-with-lorem=1";
        // Construcción del cliente
        var cliente = HttpClient.newBuilder().build();
        // Construcción de la petición
        var peticion = HttpRequest.newBuilder()
                .uri(create(url))
                .GET()
                .build();

        try {
            // Envío de la petición y obtención de la respuesta
            long tin = System.nanoTime();
            var respuesta = cliente.send(peticion, HttpResponse.BodyHandlers.ofString());
            // Ese send hace 3 cosas:
            // 1. Manda los datos de la petición al servidor remoto
            // 2. Espera a que el servidor remoto procese la petición y le devuelva una respuesta **** AQUI ESTA LO GORDO !
            // 3. Recibe los datos de la respuesta del servidor remoto
            long tfin = System.nanoTime();
            System.out.println("Tiempo de respuesta de la petición HTTP a baconipsum: " + (tfin - tin)/1_000_000 + " ms");
            // Devolución del cuerpo de la respuesta
            return respuesta.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener el texto de Bacon Ipsum";
        }

    }

    private Mono<String> pedirBaconIpsumNoBloqueante() {
        // llamada http con el cliente web reactivo de Spring
        String url = "https://baconipsum.com/api/?type=all-meat&paras=1&start-with-lorem=1";
        // Construcción del cliente
        var cliente = WebClient.create();
        // Construcción de la petición y obtención de la respuesta de forma reactiva
        long tin = System.nanoTime();
        Mono<String> respuestaMono = cliente.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext( cuerpo -> {
                    long tfin = System.nanoTime();
                    System.out.println("Tiempo de respuesta de la petición HTTP a baconipsum: " + (tfin - tin)/1_000_000 + " ms");
                });
        long tfin = System.nanoTime();
        System.out.println("Tiempo de respuesta del Mono: " + (tfin - tin)/1_000_000 + " ms");
        return respuestaMono;
    }
}
