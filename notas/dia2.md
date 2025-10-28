
# Programación funcional:

Que una variable pudiera apuntar a una función y posteriormente poder ejecutar la función desde la variable.

Para soportar esto, en java 1.8:
- paquete java.util.functions repleto de interfaces funciones, entre las que destacan:
  - Consumer<T>: recibe un parámetro de tipo T y no devuelve nada (void).
  - Supplier<T>: no recibe parámetros y devuelve un valor de tipo T.
  - Function<T,R>: recibe un parámetro de tipo T y devuelve un valor de tipo R.
  - Predicate<T>: recibe un parámetro de tipo T y devuelve un booleano.
- 2 operadores:
  - -> Expresión lambda: permite definir una función anónima.
  - :: Referencia a método: permite referenciar un método existente.

Como principales beneficios de la programación funcional tenemos:
- Crear funciones que reciban funciones como parámetros: Esto nos permite inyectar lógica a una función en tiempo de ejecución.
- Crear funciones que devuelvan funciones.

---

Principios SOLID:

5 principios que si los respetamos (podemos decidir que no lo hacemos) nos ayudarán a crear software más fácilmente mantenible y evolucionable.
- S: Single Responsibility Principle (SRP) - Principio de responsabilidad única
- O: Open/Closed Principle (OCP) - Principio de abierto/cerrado
- L: Liskov Substitution Principle (LSP) - Principio de sustitución de Liskov
- I: Interface Segregation Principle (ISP) - Principio de segregación de interfaces
- D: Dependency Inversion Principle (DIP) - Principio de inversión de dependencias

# Principio de la inversión de dependencias (DIP)

Un componente de alto nivel no debe depender de implementaciones de componentes de bajo nivel. Ambos deben depender de abstracciones (interfaces o clases abstractas).

A ninguna clase le llegan flechas (dependencias). Las flechas solo llegan a interfaces!

Y cómo podemos respetar este principio? Hay ciertos patrones que nos ayudan a respetarlo:
- Patrón Factory
- Patrón Dependency Injection

# Patrón Dependency Injection - Inyección de dependencias

Una clase nunca debe crear instancias de los objetos de los que depende. En su lugar, esas dependencias deben ser proporcionadas (inyectadas) desde el exterior.

Con esto vamos pasando el problema de unas clases a otras de más alto nivel (altura en la jerarquía del diagrama de dependencias).

Al final del camino, alguien tiene que crear esas instancias.... Hacer los new!
Si usamos un contenedor de control de inversion (IoC Container) como Spring, podemos dejarle la responsabilidad de crear esas instancias a él.

# Contenedor de Inversión de Control (IoC Container)

Es otro patrón de diseño por el cuál un desarrollador no crea el flujo de su app, sino que delega en un framework (contenedor IoC) la responsabilidad de aportar el flujo de la aplicación... y dentro de ese flujo, una parte será la creación de instancias de las clases que sean necesarias.
En ese momento, el Contenedor IoC se encargará de suministrar los datos que esas clases puedan necesitar.

---

# Springboot

Es una librería por encima de Spring que nos facilita la creación/configuración de aplicaciones basadas en Spring.
Me ofrece varias cosas:
- Starters: Paquetes de dependencias preconfiguradas para diferentes funcionalidades (web, seguridad, datos, etc).
- Auto-configuración: Configura automáticamente muchos aspectos de la aplicación basándose en las dependencias presentes en el proyecto.
- Un montón de anotaciones para simplificar la configuración y el desarrollo.
- ...

---

# Inyección de dependencias en SpringBoot

## Cómo una clase solicita una dependencia

### OPCION -1: @Autowired sobre el atributo

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public interface MiDependencia {
    void realizarAccion();
}

public class MiClase {

    @Autowired
    private MiDependencia miDependencia;

    public MiClase() {
        // miDependencia aún NO está inicializada aquí!!!!!
    }

    public void hacerAlgo() {
        miDependencia.realizarAccion();
    }
}
```

> Esto funciona solo bajo un supuesto: Que sea Spring quien crea la instancia de MiClase.

Si soy yo quien crea la instancia de MiClase, Spring no tiene oportunidad de inyectar la dependencia.
Es decir si yo hago new MiClase() , va a tener un valor miDependencia? NI DE BROMA!

Os habéis preguntado cómo hace Spring ese trabajo?

```java
// Spring necesitará ejecutar un código más o menos así:
MiDependencia dep = new MiDependenciaImpl(); // Lo que sea que se haya determinado que es apto para ser provisto cuando alguien solicita un objeto de tipo MiDependencia
MiClase instancia = new MiClase();
instancia.miDependencia = dep; // Inyección de la dependencia
```

Pregunta. Ese código compilaría? NO. miDependencia es privado. Esto no puede hacerse!
Pero de hecho se hace! Qué pasa? Spring usa REFLEXIÓN (mucha mágina).. esto, que ofrece el paquete java.lang.reflect, permite a un programa acceder a datos de los objetos directamente en memoria RAM saltándose las restricciones de visibilidad (private, protected, etc).
Esto, además de abrir una gigantesca VULNERABILIDAD de seguridad, tiene un coste en rendimiento importante (más lento).
En muchas JVMs más recientes, de hecho el paquete de reflexión está desactivado por defecto.

SU USO ESTA TOTALMENTE DESACONSEJADO. ES UNA MUY MUY MUY MUY MALA PRÁCTICA. Y herramientas como SONARQUBE os lo escupen a la cara!!!!
NO SE USA @Autowired (al menos de esa forma.. si hay otra legítima que ya os enseñaré)!!!!!!!!!

Esta forma, además de ser una mala práctica, tiene una limitación.
Si en el constructor de MiClase intento hacer uso de la propiedad, qué valor tendrá? null


### OPCIÓN 1: Pedir como argumento en cualquier función el dato / objeto que necesito.

```java
import org.springframework.beans.factory.annotation.Autowired;

public interface MiDependencia {
    void realizarAccion();
}

public class MiClase {

    public void hacerAlgo(MiDependencia miDependencia) {
        miDependencia.realizarAccion();
    }

}
```

Limpio, sin anotaciones, sin necesidad de usar reflexión, sin vulnerabilidades de seguridad, sin penalización de rendimiento.
ESTA ES LA OPCIÓN QUE USAMOS, que sonar quiera y yo también!!!!

> Esto funciona solo bajo un supuesto: Que sea Spring quien llame a mi método hacerAlgo(...) .

Si Spring es quién llama a mi método, Spring tiene la oportunidad de inyectar la dependencia.
Si soy yo quien llama a mi método hacerAlgo(...) , Spring va a darme el valor? Ni de coña.. ya es mi responsabilidad, no de Spring.

### Opción HIBRIDA! Pedir un dato en un método... pero uno muy especial: el constructor!

```java
import org.springframework.beans.factory.annotation.Autowired;
public interface MiDependencia {
    void realizarAccion();
}
public class MiClase {

    private final MiDependencia miDependencia;

    public MiClase(MiDependencia miDependencia) {
        this.miDependencia = miDependencia;
    }

    public void hacerAlgo() {
        miDependencia.realizarAccion();
    }
}
```

> Esto funciona solo bajo un supuesto: Que sea Spring quien cree la instancia de MiClase.

Esto no necesita autowired...
Y esto tiene que saltarse Spring las restricciones de seguridad que he impuesto a mi clase, para entregarme el dato que necesito?

```java
MiDependencia dep = new MiDependenciaImpl();
MiClase instancia = new MiClase(dep); // Esto no salta ninguna restricción de seguridad!!!!
```

## Cómo le indico a Spring lo que debe entregar cuando alguien solicita una dependencia

### Opción 1: Anotar la clase con @Component (o derivadas como: @Service, @Repository, @Controller, @RestController, @Job, @Processor, etc)

Algunas de esas anotaciones SOLO OFRECEN VALOR SEMÁNTICO (por ejemplo @Service, @Repository, etc).
Es decir, son componentes que al desarrollador que mira el código le digo el motivo de su existencia... lo que aportan.
SERVICIO = LOGICA DE NEGOCIO
Otras no.. Otras se ven afectadas por el flujo de la aplicación (por ejemplo @RestController).
No es solo que eso sea un controlador REST, es que por ser un controlador REST, Spring debe vincular RUTAS HTTP en el servidor de aplicación a los métodos de esa clase. (Parte del trabajo / Flujo que Spring hace al arrancar la app).

```java

public class MiDependencia  {
    void realizarAccion();
}

public class MiClase {

    private final MiDependencia miDependencia;

    public MiClase(MiDependencia miDependencia) {
        this.miDependencia = miDependencia;
    }

    public void hacerAlgo() {
        miDependencia.realizarAccion();
    }
}

@Component // <---- Anoto la clase para que Spring sepa que tiene que crear una instancia de esta clase cuando alguien la solicite
public class MiDependenciaImpl implements MiDependencia {
    @Override
    public void realizarAccion() {
        System.out.println("Acción realizada!");
    }
}
```

Qué implica esto?
Cuando Spring lea esta clase, verá la anotación @Component y sabrá que es una clase que puede ser utilizada como dependencia cuando alguien
solicite una instancia del tipo:  ~~MiDependenciaImpl~~ o **MiDependencia** (su interfaz).
Y JAMAS TRABAJARIAMOS desde fuera solicitando una instancia de MiDependenciaImpl. en el momento que hago eso ya me estoy cagando en el DIP.

> Esto solo funciona si Spring lee esta clase al arrancar la aplicación.

Y para ello, tendré que hacerle saber a Spring que quiero que lea esta Clase. Tendré que registrarla en las rutas de escaneo de Spring.
Esas rutas las puedo configurar a nivel de paquete, a nivel de clase, con patrones de expresiones regulares, etc.
La forma más habitual de establecer esto es mediante la anotación:
@SpringBootApplication, que le dice a Spring, CUANDO ARRANQUES ESTA APP, busca componentes y Configurations en este paquete y en todos sus subpaquetes.
Antiguamente, En Spring, esto es lo que hacíamos en los ficheritos XML de configuración.

> Pregunta: Qué pasa si hay varias clases que implementan la misma interfaz?

Pues depende. A priori no pasa nada. Spring registra todas.

El problema podría venir si alguien SOLICITA SOLO UNA!.. y ahí Spring, como no sabe cuál elegir, detiene la app. Y me pide información adicional!
En cambio si alguien pide las que haya, Spring le entrega todas y sin problema

```java
public interface MiDependencia {
    void realizarAccion();
}

@Component
public class MiDependenciaImpl1 implements MiDependencia {
    @Override
    public void realizarAccion() {
        System.out.println("Acción realizada por Impl1!");
    }
}              
@Component
public class MiDependenciaImpl2 implements MiDependencia {
    @Override           
    public void realizarAccion() {
        System.out.println("Acción realizada por Impl2!");
    }
}
@Component
public class MiClase {  
    private final List<MiDependencia> dependencias;

    public MiClase(List<MiDependencia> dependencias) { // Esto funciona guay
        this.dependencias = dependencias;
    }

    public void hacerAlgo() {
        for (MiDependencia dep : dependencias) {
            dep.realizarAccion();
        }
    }
}
@Component
public class MiClase2 {  
    private final MiDependencia dependencia;

    public MiClase2(MiDependencia dependencia) { // Esto es lo que hace que Spring no arranque si no tiene más información
        this.dependencia = dependencia;
    }

    public void hacerAlgo() {
        dependencia.realizarAccion();
    }
}

```
En estos casos, donde puede haber confusión, podemos usar:
- @Primary: Anotar una de las implementaciones como la principal. Da igual el resto de implementaciones, esa será la que se entregue cuando alguien solicite una instancia del tipo común. ESTO LO USAMOS BASTANTE SOLO EN LOS TEST.
- @Qualifier: Anotar la implementación con un nombre específico y luego usar ese nombre cuando alguien solicite la dependencia.
```java
@Component
@Qualifier("tipo1")
public class MiDependenciaImpl1 implements MiDependencia {
    @Override
    public void realizarAccion() {
        System.out.println("Acción realizada por Impl1!");
    }
}
@Component
@Qualifier("tipo2")
public class MiDependenciaImpl2 implements MiDependencia {
    @Override
    public void realizarAccion() {
        System.out.println("Acción realizada por Impl2!");
    }
}
@Component
public class MiClase2 {  
    private final MiDependencia dependencia;    
    public MiClase2(@Qualifier("tipo1") MiDependencia dependencia) { // Ahora Spring sabe cuál entregar
        this.dependencia = dependencia;
    }
    public void hacerAlgo() {
        dependencia.realizarAccion();
    }
}
@Component
public class MiClase3 {  
    private final MiDependencia dependencia;    
    public MiClase3(@Qualifier("tipo2") MiDependencia dependencia) { // Ahora Spring sabe cuál entregar
        this.dependencia = dependencia;
    }
    public void hacerAlgo() {
        dependencia.realizarAccion();
    }
}

@Component
public class MiClase4 {  
    private final List<MiDependencia> dependencias;
    public MiClase4(List<MiDependencia> dependencias) { // Esto funciona guay
        this.dependencias = dependencias;
    }
    public void hacerAlgo() {
        for (MiDependencia dep : dependencias) {
            dep.realizarAccion();
        }
    }
}
```
- Profiles/Perfiles, y explicitar:
 - En los componentes de la app, en qué perfiles deben estar activos. Eso se hace con la anotación @Profile("nombrePerfil")
 - Al arrancar la app indicar qué perfiles quiero activar:
   - Eso se hace en el application.properties (o application.yml) o mediante variables de entorno.
   - @ActiveProfiles("nombrePerfil"), junto con la anotación @SpringBootApplication en la clase principal de la app.


Por defecto, Spring, cuando marco una clase con @Component, me ofrece un comportamiento de tipo Singleton para esa clase.
Qué es un singleton? Un patrón de diseño que garantiza que una clase solo tenga una única instancia en toda la aplicación, y ofrece un punto global de acceso a esa instancia.

```java
public class MiClaseSingleton {
    // Las CPUs tiene cache. Los datos de un problema cuando los está ejecutando un hilo, ese hilo corre en una CPU.. y sus datos, antes de llevarlos a RAM están en la cache de la CPU... por si hacen falta en el código que se está ejecutando un poquito más adelante.
    // Si tengo 2 hilos en 2 CPUs diferentes, si cada uno mira su cache, puede que vean datos diferentes... y que en una está asignado y en otra no.
    // Con volatile le indicamos a la JVM que esa variable puede ser modificada por diferentes hilos (potencialmente en diferentes CPUs), y que evite el uso de la cache de la CPU para esa variable.
    // Esto es programación concurrent más seria!
    private static volatile MiClaseSingleton instanciaUnica;

    private MiClaseSingleton() {
        // Constructor privado para evitar instanciación externa
    }

    public static MiClaseSingleton getInstancia() {
        if (instanciaUnica == null) {  // Este if está para evitar la ejecución del sincronized salvo la primera vez... ya que computacionalmente es costoso.
            synchronized (MiClaseSingleton.class) { // Semáforo para evitar problemas en entornos multihilo.
                // Nos ayuda a evitar una CONDICIÓN DE CARRERA.
                // Genera una cola a la entrada de este bloque de código. Y solo pueden entrar los hilos de uno en uno.
                // Programación concurrent / multihilo en JAVA
                if (instanciaUnica == null) { //Que la variable esté en nula para permitir la creación de la instancia
                    instanciaUnica = new MiClaseSingleton();
                }
            }
        }
        return instanciaUnica;
    }
}
```

Spring no hace esto. No monta un Singlenton por mi.
Spring solo me garantiza que EL no va a crear más de una instancia de esa clase.... yo podría carear 17 si quiero.
Desde el punto de vista de Spring, Spring usa la clase como si fuera un singleton... sin serlo.

Y ese como digo es el comportamiento por defecto.
Ese comportamiento lo controlo con la anotación @Scope.
- @Scope("singleton") : Comportamiento por defecto. Solo una instancia en toda la app.
- @Scope("prototype") : Cada vez que alguien solicite una instancia de esa clase, Spring creará una nueva.
- @Scope("request") : Cada vez que alguien solicite una instancia de esa clase, Spring creará una nueva, pero solo durante el ciclo de vida de una petición HTTP. (Solo válido en apps web).

NOTA. SI LA OPCION 1 me sirve dejo de leer aquí. ESA ES LA BUENA

### OPCION 2: Clase de @Configuration con funciones @Bean

¿Cuándo no me valdría la opción 1?
Cuando la clase no es mia (no es de mi desarrollo... es de una librería que ya me viene).. y por tanto no puedo ir a su código a escribir @Component encima de la clase.

```java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;    

// Esto le indica a Spring que tengo aquí una clase de configuración, que puede contener definiciones de beans (componentes) que Spring podrá usar para resolver dependencias
@Configuration
public class MiConfiguracion {

    @Bean // Esto le indica a Spring que esta clase es una fábrica de beans (componentes) que Spring podrá usar para resolver dependencias
    // Cuando alguien solicite una dependencia del tipo MiDependencia, Spring ejecutará este método para obtener la instancia que debe entregar
    // Y aquí puedo usar exactamente lo mismo que os he contado para el la OPCION 1:
    // - @Primary
    // - @Qualifier
    // - @Profile
    // - @Scope
    public MiDependencia miDependencia() {
        // En este caso, soy YO quien crea la instancia de MiDependencia y la devuelvo
        return new MiDependenciaImpl(); // Aquí puedo hacer toda la lógica que necesite para crear la instancia
    }
}
```

Esto será último recurso!


---

# Flujo de la aplicación en Spring

Cuando una app arranca, y le delego a Spring el control de la aplicación:

```java
public static void main(String[] args) { 
    SpringApplication.run(MiAplicacion.class, args); // <----Aquí solicito la inversión de control a Spring
}
```

Spring empieza a ejecutar mi aplicación.. y el tiene un flujo predefinido:
1- Crea un contexto de aplicación (Application Context)
2- Lee el fichero de configuración (application.properties / application.yml)
3- Escanea el proyecto en busca de clases anotadas con @Component (y derivadas) y clases anotadas con @Configuration (y derivadas)
4- Crea el árbol de dependencias (Dependency Tree) que determina el orden en el que se crearán las instancias de las clases.
5- Crea las instancias de las clases y resuelve las dependencias 
6- Va generando avisos que yo puedo controlar mediante Listeners
7... y depende el tipo de app que esté montando hará más cosas especializadas (levantar un servidor web, abrir una sesión de consola interactiva, solicitar la ejecución de unos trabajos batch programados, etc).


Spring nos permite montar decenas de tipos de apps diferentes.
Entre ellas:
- Web completas
- Servicios REST
- Aplicaciones de consola
- Trabajos batch programados
- Aplicaciones reactivas
- Aplicaciones de escritorio (con JavaFX o Swing)

Spring tiene más de 200 librerías diferentes... y sirve para montar muchos tipos de app.

---

Un código JAVA se compila contra una versión de la JVM determinada. Y no puede ser ejecutado en JVM de versiones anteriores.
Una librería puede requerir una versión mínima de la JVM para poder ser usada.
Además con los cambios de versiones puede haber breaking changes:
Antiguamente teníamos:
- J2EE Java Enterprise Edition YA MURIÓ ESE PROYECTO ... ya no se sigue desarrollando.
   vvv
- JEE: Jakarta Enterprise Edition

Hizo cambiar un huevo de nombres de paquetes (javax -> jakarta) y rompió la compatibilidad hacia atrás.
import javax.servlet.http.HttpServlet; // Antigua
import jakarta.servlet.http.HttpServlet; // Nueva

---

JAVA a principios de los 2000 era el lenguaje estrella. TODO EL MUNDO quería aprender JAVA. Todo se hacia en JAVA.
- WEB: JAVA
- ESCRITORIO: JAVA / Swing / JavaFX
- MÓVILES: JAVA (Android)
- EMBEBIDOS: JAVA JME

25 años después, cuál es el panorama? Sigue siendo JAVA el lenguaje rey? JAVA ESTA DE CAPA CAÍDA!
- WEB: 
  - Frontal     JAVASCRIPT / TYPESCRIPT (NodeJS, Angular, React, Vue)
  - Backend:    JAVA (SPRING)... pero ojito... Kotlin (de JetBrains) está ganando mucho terreno.
- ESCRITORIO: C# / .NET / Electron / Flutter / JS
- MOVILES:  SWIFT (iOS) / KOTLIN (Android) / FLUTTER (MULTIPLATAFORMA)
- EMBEBIDOS: C / C++ / PYTHON / RUST

El problema fue ORACLE! ORACLE SE CARGO JAVA! No le interesaba!
Oracle compra SUN (Servidores-Sparc)

    - MySQL -> MariaDB
    - OpenOffice (StarOffice) -> LibreOffice
    - Hudson -> Jenkins
    - Java

Spring acaba de ser migrado completamente a Kotlin el año pasado.


Kotlin es un lenguaje que ofrece una gramática que se mea en la de java por todos los sitios.
JAVA ES UN LENGUAJE CON UNA GRAMÁTICA DE MIERDA LLENA DE AGUJEROS! Que lo pensaron como el culo!


 JETBRAINS (los creadores de INTELLIJ IDEA) son los creadores de KOTLIN. Que saben un huevo de GRAMATICAS Y LENGUAJES, hacen un lenguaje DE LA HOSTIA.

    .kt ->. compila a .class -> JVM
    por lo que aprovechan TODA LA INFRAESTRUCTURA y el ecosistema de JAVA.

    Kotlin es simplemente una sintaxis mejorada para programar en la JVM.
    Scala es otro lenguaje que también compila a JVM, pero su gramática es un poco más rara.

```java
public class Persona{
    
    private String nombre;
    private int edad;

    public Persona(String nombre, int edad){
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre(){
        return nombre;
    }
    public int getEdad(){
        return edad;
    }

    public int getEdad(){
        return edad;
    }   
    public void setEdad(int edad){
        this.edad = edad;
    }

}
```

```kotlin
data class Persona(val nombre: String, var edad: Int)
```

```java
// Día 1
public class Persona{
    
    public String nombre;
    public int edad;

    public Persona(String nombre, int edad){
        this.nombre = nombre;
        this.edad = edad;
    }

}
// Dia 2-100
Persona p = new Persona("Pepe", 30);
p.edad = 31;
System.out.println(p.nombre + " tiene " + p.edad + " años.");

//. Dia 101 y digo yo.. unmmmm! voy a hacer que la edad no pueda ser negativa!
// Y tocate los huevos Manolo que no hay sitio donde meter eso! <<< ESTA LA MIERDA DE JAVA

public class Persona{
    
    public String nombre;
    private int edad;

    public Persona(String nombre, int edad){
        this.nombre = nombre;
        this.setEdad(edad); // Uso el setter para validar
    }

    public int getEdad(){
        return edad;
    }

    public void setEdad(int edad){
        if(edad >= 0){
            this.edad = edad;
        } else {
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
    }

}
// Dia 102: Que tengo a 10k personas kalasnikov en mano porque su código no compila y le he jodido!
```
Y para evitar esto, en JAVA la BUENA PRACTICA ES:
- Aunque no vayas a meter HOY ningun control sobre un atributo, siempre hazlo privado y crea getters y setters.
- ASI SI ACASO el día de mañana tienes que meter lógica de control, ya tienes el sitio donde meterla, y no jodes a nadie.

Los getters y lo setters están ahí solo para facilitar la mantenimiento futuro del código.
LO CUAL ES UNA MIERDA... porque me obligan a el dia 0 crear 200 funciones que no necesito.


```py

class Persona:
    def __init__(self, nombre: str, edad: int):
        self.nombre = nombre
        self.edad = edad  # Atributo "privado" (convención)


p = Persona("Pepe", 30)
p.edad = -5  # No hay control, pero puedo usar propiedades para añadirlo
print(f"{p.nombre} tiene {p.edad} años.")

#Version 2:

class Persona:
    def __init__(self, nombre: str, edad: int):
        self.nombre = nombre
        self._edad = edad  # Atributo "privado" (convención)

    @property
    def edad(self) -> int:
        return self._edad

    @edad.setter
    def edad(self, valor: int):
        if valor >= 0:
            self._edad = valor
        else:
            raise ValueError("La edad no puede ser negativa")

#Pero la gente sigues escribiendo:
p = Persona("Pepe", 30)
p.edad = -5  # No hay control, pero puedo usar propiedades para añadirlo
print(f"{p.nombre} tiene {p.edad} años.")
```

```csharp
public class Persona
{
    public string nombre;
    public int edad;

    public Persona(string nombre, int edad)
    {
        this.nombre = nombre;
        this.edad = edad;
    }

}

// V2

public class Persona
{
    public string nombre;
    private int _edad {
        get { return _edad; }
        set
        {
            if (value >= 0)
            {
                _edad = value;
            }
            else
            {
                throw new ArgumentException("La edad no puede ser negativa");
            }
        }
    }

    public Persona(string nombre, int edad)
    {
        this.nombre = nombre;
        this.Edad = edad; // Uso la propiedad para validar
    }

} 
```


---

Montaremos un servicio WEB REST con SPRINGBOOT usando JAVA.
Va a ser el típico CRUD de GESTIÓN DE ANIMALITOS...de una tienda de animales
/api/v1/animales
- GET /api/v1/animales -> Listar todos los animalitos
- GET /api/v1/animales/{id} -> Obtener un animalito por su id
- POST /api/v1/animales -> Crear un nuevo animalito
- PUT /api/v1/animales/{id} -> Actualizar un animalito por su id
- DELETE /api/v1/animales/{id} -> Eliminar un animalito por su id

Persistencia en BBDD Relacional: JPA

Con 7 vamos bien!

                                               JAVA
                        ControladorRest ----> Service ----> Repository ----> BBDD

RESPONSABILIDAD         Exponer el            Lógica        Gestionar la     Garante del 
                        servicio por REST     de negcio     persistencia       dato

                        ControladorSoap ---->

                        ControladorRestV2 -->

                        ControladorWebSocket->

A mi suena Sillín, Ruedas, sistema de frenado.. y bici... un sistema con un montón de piezas.

API Repositorio                         animalitos-repository.jar
Implementación Repositorio JPA          animalitos-repository-jpa.jar

API Servicio                            animalitos-service.jar
Implementación Servicio                 animalitos-service-impl.jar

API Controlador                         animalitos-controller.jar
Implementación Controlador              animalitos-controller-impl.jar

Aplicación Principal                    animalitos-app.jar



                                                                        animalitos-repository.jar <----- animalitos-repository-jpa.jar
                                                                                  ^                            ^
                                                                                  |                            |
                                        animalitos-service.jar <---- animalitos-service-impl.jar               |
                                                 ^                                ^                            |
                                                 |                                |                            |
   animalitos-controller.jar <---  animalitos-controller-impl.jar                 |                            |
                                                 ^                                |                            |
                                                 |                                |                            |
                                                 +--------------------------------+----------------------------+
                                                                                  |
                                                                        animalitos-app.jar
                                                                        BICICLETA COMPLETA


Aquí estamos llevando el concepto / principio de La inversión de dependencias a nivel arquitectónico. A nivel de los MODULOS DE MI APP.

Y Aquí está el turrón!

El sistema de frenos (ServicioImpl) depende (trabaja en conjunto con) las ruedas (Repositorio).
Al sistema de frenos le importa 3 narices las ruedas concretas que le ponga entre las pinzas de freno... sienmpre y cuando cumplan con una específicación  (API Repositorio)... tener un determinado ancho de llanta, un determinado diámetro, etc.

Mi bici, no trabaja con especificaciones. Mi bici ya la monto con :
- Unas ruedas concretas michelin: animalitos-repository-jpa.jar
- Un sistema de frenos concreto shimano: animalitos-service-impl.jar
- Un sillín concreto: animalitos-controller-impl.jar

Y a mi bici/aplciación, el día de mañana le debo poder cambiar las ruedas por otras (otra implementación del API Repositorio) sin que el sistema de frenos (ServicioImpl) se entere de nada. Pero a mi apliación/bici, le pongdré otras ruedas concretas: animalitos-repository-mongo.jar


FLUJO DE DATOS?

    Animalito -> BBDD -> Interfaz // animalitos-repository.jar
        id        number autogenerado   --- ese id no se expone fuera de la capa de persistencia: VULNERABILIDAD DE SEGURIDAD
        publicId  string uuid       --- este id es el que se expone fuera de la capa de persistencia
                            uuidv4: 550e8400-e29b-41d4-a716-446655440000
        nombre    string
        especie   string
        edad      int
        descripcion string


    AnimalitoEntity implementa Animalito  // animalitos-repository-jpa.jar
    {
        Long id;  <--- privado de la capa de persistencia
        String publicId;  <--- uuidv4
        String nombre;
        String especie;
        int edad;
        String descripcion;
    }
            vvvvv    MAPPER (MapStruct) OJO Cuando la configuremos.. también usaremos lombok... y hay que configurarlas bien!

    AnimalitoDTO // animalitos-service.jar
    {
        String publicId;
        String nombre;
        String especie;
        int edad;
        String descripcion;
    }

            vvvvv     MAPPER (MapStruct)

    Lo que mando por el controlador (HTTP) REST... JSON... DTO (Data Transfer Object) // animalitos-controller.jar

    class AnimalitoRestV1DTO (interfaz) // No llevan código por ser DTOs.. y los defino como parte del API
    {
        "id" <--- publicId
        "nombre" <--- nombre
        "especie" <--- especie
        "edad" <--- edad
        "descripcion" <--- descripcion
    } 


 # RestV1 ???    
    REST
    v1?  Es el major del API controlador REST


Las versiones en el mundo del software no solemos escribirlas con semver?
Esquema semantico de versiones?

va.b.c  El api tendrá una versión de este estilo.


a MAJOR     Breaking changes (cambios que rompen la compatibilidad hacia atrás)
b minor 
c patch

---

> Por donde empezamos?

Por la documentación XD XD.. Me encanta. Buen sitio! 
Si hacemos un top-> bottom: Controller -> Service -> Repository -> BBDD
                                |
                                v
                            Swaggerv1,2 --> OpenAPI (v3 swagger)

Hacer un desarrollo bottom -> up

Depende del nivel de conocimiento que tenga del proyecto me interesa más uno u otro.
Si control lo que quiero hacer, empiezo el repositorio.
Si no controlo mucho, empiezo por el controlador REST y voy bajando.

- Vamos a empezar por abajo... Y luego pasamos Arriba

    Repository -> Controller -> Service

- Empezando por capa de Repository... por donde empezamos?
  - API *
  - Implementación JPA

Una vez tenga el API -> Pruebas de la implementación JPA -> Poder hacer la implementación JPA

TDD (Test Driven Development)
- Metodología de desarrollo (cómo el desarollador se pone a picar código)
  - 1. PRUEBAS QUE SE PONGAN EN ROJO: FALLEN
  - 2. PICO EL CÓDIGO MÍNIMO PARA QUE LAS PRUEBAS SE PONGAN EN VERDE
  - 3. REFACTORIZO EL CÓDIGO (MEJORA DE CÓDIGO SIN CAMBIAR SU FUNCIONALIDAD)


Qué pruebas vamos a hacer sobre el Repositorio? Tipos de pruebas
- Unitarias



---

# Testing?

## Vocabulario en el mundo del testing:

- Causa Raíz    Motivo por el cuál el humano ha cometido un error
- Error         Los humanos cometemos errores (errar es humano). Las máquinas cometen errores
                Esos errores tendrán un motivo: La causa raíz
- Defecto       Como consecuencia de mi error puedo haber introducido un defecto (bug) en el código
- Fallo         La manifestación del defecto en tiempo de ejecución. Una desviación del comportamiento esperado

## Para que sirven las pruebas?

- Asegurar el cumplimiento de unos requisitos FUNCIONALES o NO FUNCIONALES.
- Tratar evitar que a producción lleguen muchos DEFECTOS.
  Esos defectos es lo que intento arreglar. Para ello, lo primero es identificarlo:
    - Pruebas que traten de provocar FALLOS - Pruebas dinámicas
    - Pruebas que buscan directamente fallos - Pruebas estáticas (SonarQube, etc.)
- Aportar información que ayude al desarrollador a hacer una rápida identificación del defecto en caso de haberse identificado un fallo. DEPURACION o DEBUGGING
- Saber que tal voy! En una metodología AGIL
- Aprender de mi producto... para aplicar a futuros productos.
- Para hacer un análisis de causas raíces de los errores humanos que han provocado los defectos. Y tomar acciones preventivas.
- ...

## Tipos de pruebas:

Hay muchas formas de clasificar las pruebas.. Todas válidas y paralelas entre si.
Taxonomías de clasificación de pruebas:

# En base al objeto de pruebas
- Pruebas funcionales:      Req funcionales
- Pruebas no funcionales:   Req no funcionales
  - Seguridad
  - Rendimiento
  - Carga
  - Estrés
  - UX
  - ...

# En base al conocimiento sobre el funcionamiento del objeto de prueba

- Pruebas de caja negra: No conozco o no uso el funcionamiento interno del objeto de prueba
- Pruebas de caja blanca: Conozco el funcionamiento interno del objeto de prueba

# En base al procedimiento de ejecución:

- Pruebas manuales
- Pruebas automáticas

- Pruebas estáticas: Las que no requieren la ejecución del objeto de prueba
- Pruebas dinámicas: Las que requieren la ejecución del objeto de prueba

# en base al alcance de la prueba / SCOPE

- Unitarias             Se centran en un COMPONENTE AISLADO de mi sistema

      Sistemas de frenos. Probar... no tengo nada más... solo el sistema de frenos. puedo probarlo? SI Y DEBO

                    Cómo lo pruebo?
                    - GIVEN.   DADO (Contexto)
                      Monto el sistema de frenos en un bastidor (4 hierros mal soldaos) y pongo un sensor de presion entre las pinzas de freno. 
                    - WHEN     CUANDO (Acción)
                      Cuando aprieto la palanca de freno         .apretarPalanca(); 
                    - THEN.    ENTONCES (Resultado esperado)
                      Las pinzas deben cerrar y hacerlo con una fuerza de X Newtons.

                Si hago esta prueba y va bien, eso significa que la bici va a funcionar? NO
                Entonces pa' que la hago? Qué gano? CONFIANZA +1. Vamos bien!
      ... Sillín   Qué pruebo?
                        Lo monto en un bastidor:
                            - Si aguanta a un tio/a de 150Kgs                                               Prueba de carga
                            - Que después de 4 horas no le duela el culo                                    Prueba de UX
                            - Que despuñés de sentarse y levantarse 500 veces, el cuero no se desgaste      Prueba de estres
      ... Ruedas
      ... cuadro
      ... pedales

- Integración           Se centra en la COMUNICACION entre 2 componentes de mi sistema

        Sistema de frenos y las ruedas. Puedor probarlas juntas? SI Y DEBO

                    Cómo lo pruebo?
                    - GIVEN.   DADO (Contexto)
                      Monto el sistema de frenos en un bastidor (4 hierros mal soldaos) pongo la rueda en medio de las pinzas de freno y le pego un viaje a la rueda
                    - WHEN     CUANDO (Acción)
                      Cuando aprieto la palanca de freno         .apretarPalanca(); 
                    - THEN.    ENTONCES (Resultado esperado)
                      Entonces la rueda debe frenarse en X segundos.

                Y mira que no! Que resulta que las pinzas cierran , pero la rueda es muy estrecha y las pinzas no llegan a tocar la llanta!
                CAGADA!
                El sistema de frenos está mal? NO
                La rueda está mal? NO
                Simplemente juntas no funcionan.
                El sistema de frenos no es capaz de COMUNICAR la energía de rozamiento a la rueda.

                Si hago esta prueba y va bien, eso significa que la bici va a funcionar? NO
                Entonces pa' que la hago? Qué gano? CONFIANZA +1. Vamos bien!


- Sistema (end2end)      Se centra en el COMPORTAMIENTO del sistema en su conjunto
                Cojo a un tio, le enchufo en la mochila agua y bocadillo de chorizo y pa cuenca! 3 horas!

                Y vuelve, sano y salvo, con el culo a gustuito!

                Si hago esta prueba y va bien, eso significa que la bici va a funcionar? SI

                Pregunta... y si hago estas pruebas y van bien.. necesito hacer pruebas unitarias y de integración? NO... ya pa'que

                Truco:
                - Y si van mal? Dónde está el problema?
                - Cuando puedo hacerlas? Cuando tengo ya la bici! y hasta enconceto.. voy a ciegas?


- bastidor (4 hierros mal soldaos)  \ Test Doubles: Mocks, Stubs, Fakes, Spies, Dummies < Mockito + Spring Test
- sensor de presion                 /

Esas cosas no son parte de la bici.. pero si del proyecto... y tendré que presupuestarlo y contabiliozarlo como gasto!

---

> El software funcionando es la medida principal de progreso. >>> DEFINIR UN INDICADOR PARA UN CUADRO DE MANDO!  PRUEBAS
   
   NUCLEO
   ------
La medida principal de progreso es el software funcionando.
------------------------------- ---------------------------
SUJETO                           PREDICADO   

La forma en la que vamos a medir que tal va nuestro proyecto es mediante el concepto SOFTWARE FUNCIONANDO.

SOFTWARE FUNCIONANDO = Software que funciona, que hace lo que tiene que hacer.
Quién dice que el software está funcionando? 
- ~~El cliente~~ 
- LAS PRUEBAS me dice si hace lo que tiene que hacer (si cumple con los requisitos funcionales y no funcionales).

---

# DEVOPS?

Cultura, filosofía, movimiento en pro de la automatización!

- Integración Continua (CI - Continuous Integration)

   Tener CONTINUAMENTE la última versión del código que han hecho los desarolladores en eun entorno de INTEGRACION sometido a pruebas automatizadas para producir en tiempo real un INFORME DE PRUEBAS. Eso es montar un pipeline de CI.

- Continuous Delivery (CD - Entrega Continua)

   Que mi cliente tenga en su mano siempre la última versión del software que ha sido validada en el entorno de integración continua, y esté lista para ser desplegada en producción en cualquier momento. Dejar mi producto en automático en un artifactory, docker hub, maven central, app google play, apple store, etc.

- Continuous Deployment (CD - Despliegue Continuo)

   Que mi cliente tenga instalada en producción la última versión del software que ha sido validada en el entorno de integración continua, en cuanto esa versión pasa todas las pruebas automatizadas. Montar un pipeline de CD que coja la última versión del software validada en CI y la despliegue automáticamente en producción.


---

1. Montar el API del Repositorio 
    interfaz: Animalito
    interfaz: AnimalitoRepository
2. Montar el proyecto de implementación
    - Montar las pruebas unitarias del Repositorio (H2 memory)
    - Montar la implementación JPA del Repositorio
    - Verificar que las pruebas pasan OK
3. Montar el API del controlador: OPENAPI SWAGGER
    - interfaz AnimalitoRestV1DTO + otras 2 o 3 DTOs
    - interfaces de las validaciones ( DTOS )
    - interfaz controlador, donde definamos el API REST: endpoints, verbos HTTP, códigos de respuesta, etc. (OPENAPI)
    - generar el openapi.yaml
4. Montar el proyecto de implementación del controlador
    - Montar las pruebas unitarias del controlador (Mocks)... y pruebas de integración (Spring Test WebMVC)
    - Montar los mappers (MapStruct)
    - Montar la implementación del controlador REST ---> API Servicio
    - ControlerAdvice (gestión de errores)
    - Pruebas de integración del controlador con la implementación del Servicio. Mockito para mockear el repositorio
5. Montar el API del Servicio
    - interfaz AnimalitoDTO + otras 2 o 3 DTOs
    - interfaz AnimalitoService
6. Montar el proyecto de implementación del Servicio
    - Montar las pruebas unitarias del Servicio (Mocks)
    - Montar la implementación del Servicio ---> API Repositorio
    - Montar los mappers (MapStruct)
    - Pruebas de integración del Servicio con la implementación del Repositorio (H2 memory)
7. Montar la aplicación principal
    - Configuración HTTP, Seguridad, etc.
    - Dare de alta los controladores REST (Hoy el de animales.. mañana quien sabe)
    - Montar pruebas de sistema (end2end) con Cucumber

Esto que he estado escribiendo no era para vosotros!  XD XD


EL PROMPT AL HABLAR CON UNA IA no vale para mierda!

Las ias se alimenta de 2 cosas:
- Prompt
- Contexto <<<<< AQUI ES DONDE DEBEMOS PONER LA CARNE!