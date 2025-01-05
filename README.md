# Reactive Workshop
Este proyecto es desarrollado para demostrar la familiaridad con las herramientas de desarrollo que hacen parte del
nuevo stack tecnológico.

## Índice
1. [Herramientas utilizadas](#herramientas-utilizadas)
2. [Configuración y ejecución](#configuración-y-ejecución)
3. [Lista de endpoints](#lista-de-endpoints)

---

## Herramientas utilizadas
Lista de herramientas utilizadas para el desarrollo del proyecto:
- **Java**: 17.0.12
- **Gradle**: 8.11.1
- **Docker**: 27.4.1
- **Docker compose**: 1.29.2
- **Bases de datos**: Postgresql mediante el adaptador rdbc.
- **Plugins**: Scaffold Clean Architecture

---

## Configuración y ejecución
1. Descargue el proyecto mediante HTTPS o SSH en su equipo.
2. Ubíquese en la raíz del proyecto y ejecute el siguiente comando:
    ```bash
    docker-compose up -d
    ```

Eso es todo. ¡Estamos listos para probar nuestra aplicación! 🚀

Luego de probar los endpoints, podemos detener nuestra aplicación mediante el siguiente comando:
```bash
docker-compose down -v
```

## Lista de endpoints

1. Crear un usuario
   localhost:8080/api/users/create/{id} 
    @PathVariable id -> Id del usuario que buscaremos en reqres
2. Buscar un usuario
    localhost:8080/api/users/find/{id}
    @PathVariable id -> Id del usuario que buscaremos en nuestra base de datos
3. Listar todos los usuarios
    localhost:8080/api/users/list
4. Filtrar la lista de usuarios por nombre
    localhost:8080/api/users/filter
    @Param firstName -> Primer nombre mediante el cual filtraremos