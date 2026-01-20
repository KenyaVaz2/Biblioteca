Sistema Web Bibliotecario

&nbsp;Descripción



Sistema web orientado a la gestión integral de bibliotecas, diseñado bajo una arquitectura cliente–servidor y control de acceso basado en roles.

El sistema permite la consulta y administración del acervo bibliográfico, la búsqueda avanzada local e interbibliotecaria, así como la gestión del ciclo de préstamos, devoluciones y multas.



Cuenta con dos interfaces diferenciadas:



Usuario: consulta del catálogo, solicitudes de préstamo y seguimiento de estado.



Administrador: gestión del acervo, usuarios, préstamos y multas.



Módulos del sistema:



* Búsqueda bibliográfica
* Búsqueda interbibliotecaria
* Solicitud y control de préstamos
* Gestión de devoluciones
* Cálculo y administración de multas
* Control de acceso por roles (Usuario / Administrador)



Arquitectura



El sistema está estructurado bajo un enfoque modular y de separación de responsabilidades:



Biblioteca/

│── cliente/    → Frontend (React)

│── servidor/   → Backend (Spring Boot)





* Cliente: interfaz web desarrollada en React.
* Servidor: API REST desarrollada en Spring Boot.



Comunicación mediante HTTP/JSON.



Tecnologías utilizadas:

Backend: Java

Spring Boot: API REST



Control de roles y lógica de negocio



* Frontend: React

JavaScript

HTML / CSS







* Roles del sistema

Rol	Funcionalidades:

Usuario	Búsqueda, solicitud de préstamo, consulta de multas

Administrador	Gestión de libros, usuarios, préstamos y multas



* Control de versiones

Este proyecto utiliza Git para el control de versiones y se aloja en GitHub, empleando la rama main como rama estable.



Estado del proyecto



En desarrollo – nuevas funcionalidades y mejoras en proceso.



Autor



Kenya Marisol Vazquez Salto 

Proyecto académico / personal

