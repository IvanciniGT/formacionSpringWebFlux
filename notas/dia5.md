


Asincronía / Sincronía
--------------------------

Formas de establecer una comunicación entre 2 partes:
- Síncrono: El que manda el mensaje queda a la espera de una respuesta por parte del destinatario. Ejemplo: llamada telefónica. HTTP
- Asíncrono: El que manda el mensaje no queda a la espera de una respuesta... sigue haciendo sus cosas. Ejemplo: email, SMS, WebSockets.

Esto lo podemos ver dentro de un código:

TAREA 1
TAREA 2 ---> Implica comunicación con otro
                - Fire-and-forget: Yo le mando mi mierda y me la pela lo que tenga que decir al respecto.. en algunos casos puede tener sentido... en general en pocos.
                - Necesito confirmación de recepción: Aquí necesito que me confirme que ha recibido mi mensaje. \
                - Necesito respuesta: Aquí necesito que me responda con algo concreto.                          / Esto puede ir por separado... o no    WHATSAPP (separado √√)
TAREA A (necesita datos de la tarea 2)
TAREA B (no necesita nada de la tarea 2)

El tema ahora es, si no quiero hacer un fire & forget, 
Me espero a hacer la tarea 3 hasta que la tarea 2 me confirme que ha recibido mi mensaje ?
    Si no se recibe.. insisto!
Me espero a hacer la tarea 3 hasta que la tarea 2 me responda con algo concreto?

---

TAREA 1
--> TAREA 2 (async)
TAREA B
AWAIT TAREA 2            En JAVA Esto está implementado con los Future<T> (1.5) .get()
TAREA A

El tema es que si necesito la respuesta para algo (en nuestro caso para la tarea A)... tengo 2 opciones:
1. La de arriba... que es Cuando ya haya hecho todo lo que puedo hacer sin la respuesta, espero a que me llegue la respuesta y hago lo que tenga que hacer con ella. ASINCRONIA... La tarea 2 se ejecuto asíncronamente respecto a la tarea B.
   El problema de esta estrategia es que en algún momento me quedo de brazos cruzados antes futuros trabajos que puedan llegar.
   Cuando me cruce de brazos (AWAIT TAREA 2) no tenía más trabajos que ejecutar que la tarea A... y para esa, necesitaba la respuesta de la tarea 2... pero el problema es que en ese momento llegue una tarea C, y esa tarea no necesita nada de la tarea 2... pero tarde... yo ya me eché la siesta! Y no podré procesar la tarea C hasta que me llegue la respuesta de la tarea 2.

TAREA 1
--> TAREA 2 (async, cuando esté lista2: ejecutar TAREA A... que me avisen para que lo haga)
TAREA B


2. En este modelo, yo no estoy esperando a nadie. Cuando la tarea 2 esté lista, me avisarán y REACCIONARÉ a ello (REACTIVE)
   Puedo ejecutar la tarea B.. y sigo libre después... no estoy esperando a la tarea 2.... No estoy en la puerta de la tintorería con mi ticket en la mano a ver si me entregan la camisa... Ya avisa allí que cuando mi camisa esté lista, me llamen
   Y eso me deja libre para acometer nuevas tareas que vayan llegando: TAREA C, TAREA D, etc...
   Distinto es el caso 1... que es que cuando acabe lo que tenía pendiente hasta ese momento, decidí ir a la puerta de la tintorería y esperar allí... Eso me incapacita para hacer nuevas tareas que estén llegando a la casa!

   Esto es lo que en Java no existe a nivel del API... y lo que me resuelve la librería Project Reactor: Mono / FLux

Estas 2 son Asíncronas en la llamada
Future<T>         .get()                  ->  Bloqueante (Punto de sincronía)
Mono<T>           .map( FUNCION )         ->  No bloqueante (Asincronía / Reactividad)
                          ^
                          Lo que debe ser ejecutado cuando el Mono esté listo... que ya de antemano lo he dejado programado


Alguien siempre va a tener que esperar... pero no quiero ser yo! que sea otro! Que sea el tio que está accediendo con su teléfono a mi web o a recibir un email...


    CLIENTE       ->          CAMARERO       ->         COCINA


Asincronía, desde el punto de vista del camamero, el cliente le ha hecho un pedido. Y el tiene 2 trabajos:
1. Llevarlo a la cocina (el pedido)
2. Recoger el pedido cuando esté listo y llevárselo al cliente

En paralelo él tendrá que atender a más clientes.

Opciones del camarero:
OPCION A : Atiende al cliente1, lleva su comanda a la cocina.. se queda en la puerta esperando que esté lista la comida... cuando está lista, se la lleva al cliente1... y luego atiende al cliente2
    SINCRONO PURO!
OPCION B: Atiende al cliente1, lleva su comanda a la cocina.. y se va a atender al cliente2... acaba de atender al cliente 2... y no hay más clientes... o aunque los haya, decide porque ya ha pasado mucho tiempo ir a la cocina a recoger el pedido del cliente 1....y espera (BLOQUEO) FUTURO .get().  El camarero estaría siendo PROACTIVO
    ASINCRONÍA INICIAL.. pero luego tienes un bloqueo
OPCION C: Atiende al cliente1, lleva su comanda a la cocina.... y les dice: OYE, CUANDO ESTÉ LISTO EL PEDIDO DEL CLIENTE1, AVISADME QUE LO RECOJO... y yo ya me olvido del tema.. es más, me avisas a mi o a cualquiera que haya por aquí, que no tengo por que ser yo.
Y yo sigo..,. ya totalmente despreocupado... atendiendo a más clientes... y cuando me avisen, recojo el pedido si es que me avisan... si no han avisado a otro... Ya no es mi problema
    ASINCRONÍA TOTAL... REACTIVIDAD    El camarero es REACTIVO... el reacciona a los avisos que le van llegando... pero PROACTIVAMENTE NO VA A LA COCINA A POR EL PEDIDO...