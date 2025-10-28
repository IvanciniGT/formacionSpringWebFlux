

def saluda ():          # Definir una función
    print("Hola")

saluda()                # Llamar a la función


nombre = "Ivan"         # Asignar una variable a un valor

# String nombre = "hola"; # Asignar una variable a un valor
    # 1.                         "Ivan"     Crea un dato en Memoria RAM de tipo String con valor "hola"
    #                               La RAM Es como un cuaderno de cuadricula (de hecho literalmente es lo que es)
    # 2.                         String nombre.  
    #                               Creando una variable de tipo String. String no es el tipo del dato. Es el tipo de la variable
    #                               Java es un lenguaje de tipado estático. Python es un lenguaje de tipado dinámico.  
    #                               En python las variable NO TIENE TIPO. Y por ende pueden apuntar a datos de cualquier tipo.
    #                               Lo que tendrá tipo es el dato al que apunta la variable.. que puede cambiar (no el tipo el dato!)
    #                               En Java, por ser de tipado estático, una variable TIENEN TIPO..y solo pueden apuntar a datos de ese tipo.
    #                               Si la RAM Es como un cuaderno de cuadricula, una variable es como un post-it. 
    #                               En el post-it escribo "nombre", no el dato.. el dato no está en la cvariable
    #                               El dato está en el cuaderno.. en la RAM
    # 3                         =   Asignar la variable al dato (pego el post-it en la cuadricula del cuaderno al lado del dato)
# nombre ="Adios";
#         1.     "Adios"    Crea un nuevo dato en Memoria RAM de tipo String con valor "Adios"
#                           Dónde? En el mismo sitio donde estaba "hola" o en otro"? EN OTRO !!!!
#         2.    nombre=     Despego el post-it "nombre" de la cuadricula del cuaderno donde estaba pegado y lo pego en la cuadricula del cuaderno donde está el dato "Adios"
#                           Lo que vario es la variable... que ahora punta a otro lado/DATO
# JAVA: nombre = 33;        Error de compilación. No puedo asignar un dato de tipo diferente al tipo de la variable
#
# JAVA 11 : Introduce la palabrabreja var
#        var nombre = "Hola";   // Inferencia de tipos
#        nombre = 33;           NO ES VALIDO. Java sigue siendo de tipado estático, y lo que pasa cuando usamos var es que la variable toma el tipo del dato 
#                               del primer dato al que se asigna. En este caso String. Y luego no puede cambiar.
nombre = 33

# Qué es una variable? En Java, Python, en JS una variable es una referencia a un dato que tengo en RAM... tiene más que ver con el concepto de puntero en C/C++.

nombre = saluda        # Estoy ejecutando la función saluda? NO.. No tiene parentesitos detrás. Igual que en java, para ejecutar una función hay que ponerle paréntesis
# Simplemente asigno mi VARIABLE a la función... Igual que antes mi variable apuntaba a un texto o a un número, ahora apunta a una función.

nombre()               # Ahora sí la ejecuto. Llamo a la función a través de la variable.

# Pa' que vale esta mierda?

def generar_saludo_formal(nombre):   # Función que recibe un parámetro
    return "Buenos días Sr. " + nombre

def generar_saludo_informal(nombre): # Función que recibe un parámetro
    return "Hola " + nombre + ", ¿qué tal?"

def imprimir_saludo(funcion_generadora_saludo, nombre): # Función que recibe como parámetro otra función
    saludo = funcion_generadora_saludo(nombre)  # Llamo a la función que me han pasado como parámetro
    print(saludo)

imprimir_saludo(generar_saludo_formal, "Ivan")     # Paso como parámetro la función generar_saludo_formal
imprimir_saludo(generar_saludo_informal, "Ivan")   # Paso como

# No confundir con:
#imprimir_saludo(generar_saludo_formal("Ivan"))    # En este código, que recibiría imprimir_saludo? Un string
# Y cuando se ejecuta generar_saludo_formal("Ivan") ? antes o después de imprimir_saludo?

# Y esto es una locura.. porque lo que estamos haciendo es INYECTAR LOGICA A UNA FUNCION.
# El día que creo la función imprimir_saludo no sé la lógica de composición del saludo. Ni me importa.. que me la den... 
# QUE DEN ESA LOGICA.
# Porque mio responsabilidad es imprimir el saludo.. no aportar la lógica de composición del saludo.
# También es responsabilidad mia usar esa lógica que me den para generar el saludo.
# Pero no la generación en si misma.
# Esa lógica, la inyecto yo en la función, en tiempo de ejecución.

# Y cada día, más y más funciones del API de Java, y PYTHON, Y JS admiten /obligan a suministrar funciones como parámetros.