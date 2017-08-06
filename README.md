# JAAS-Tomcat-Auth

Sistema de autenticación y autorización utilizando JAAS y Apache-Tomcat
URL aplicación: http: // localhost: 8080 / ID_PRAC1 / protected /

Contenido de las carpetas
=========================

Sólo se muestra el listado de los archivos y carpetas más importantes a tener en cuenta para el funcionamiento de la
aplicación. Existen más archivos, pero no tienen tanta relevancia al tratarse de objectos que
vienen por defecto en la instalación de Tomcat.

/
--apache-tomcat-7.0.32 /
---- bin / Carpeta que contiene los archivos necesarios de Tomcat para
su correcta ejecución, así como algunos scripts
------setenv.sh Script para definir el archivo de configuración de JAAS
------shutdown.sh Script para parar Tomcat
------startup.sh Script para iniciar Tomcat
---- conf /
------ jaas.config Configuración de la clase a utilizar para la autenticación
------ rols_accions.txt Fichero que contiene la relación entre roles y acciones permitidas
------ shadow.txt Archivo que contiene las contraseñas de los usuarios
------ users.txt Archivo que contiene la información básica de los usuarios
---- lib /
------compila_id_prac1.sh Script para compilar los archivos fuente de las diferentes clases
------Autenticador.java Archivo fuente de la clase autenticador, que permite comprobar
que el usuario se ha autenticado con unas credenciales correctas
------ Autenticador.class Binario de la clase autenticador
------IDLoginModule.java Archivo fuente de la clase IDLoginModule que permite realizar
todas las operaciones relacionadas con la autenticación
------ IDLoginModule.class Binario de la clase IDLoginModule
------IDPrincipalRol.java Archivo fuente de la clase IDPrincipalRol que define un
Principal del tipo rol
------ IDPrincipalRol.class Binario de la clase IDPrincipalRol
------IDPrincipalUsuari.java Archivo fuente de la clase IDPrincipalRol que define un
Principal del tipo usuario
------ IDPrincipalUsuari.class Binario de la clase IDPrincipalUsuari
------Usuari.java Archivo fuente de la clase Usuario, que permite obtener toda
la información de los ficheros que contienen estos datos
------ Usuari.class Binario de la clase Usuario
---- webapps /
------ ID_PRAC1 /
-------- protected /
---------- error.jsp Página de error de nombre de usuario y / o contraseña
---------- index.jsp Página principal de la aplicación, desde la que se accede a los
diferentes módulos que la forman
---------- login.jsp Página de introducción de nombre de usuario y contraseña
------------ compras /
-------------- index.jsp Página principal del módulo de compras, que muestra las acciones
permitidas al usuario según los roles asignados
------------ inventario /
-------------- index.jsp Página principal del módulo de inventario, que muestra las acciones
permitidas al usuario según los roles asignados
------------ rrhh /
-------------- index.jsp Página principal del módulo de RRHH, que muestra las acciones
permitidas al usuario según los roles asignados
------------ ventas /
-------------- index.jsp Página principal del módulo de ventas, que muestra las acciones
permitidas al usuario según los roles asignados
-------- WEB-INF /
---------- web.xml Contiene la configuración de la autorización de acceso a la aplicación

