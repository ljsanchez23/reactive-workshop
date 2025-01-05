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
   **URL:** `localhost:8080/api/users/create/{id}`  
   **Parámetro:** `@PathVariable id` -> Id del usuario que buscaremos en reqres.

2. Buscar un usuario  
   **URL:** `localhost:8080/api/users/find/{id}`  
   **Parámetro:** `@PathVariable id` -> Id del usuario que buscaremos en nuestra base de datos.

3. Listar todos los usuarios  
   **URL:** `localhost:8080/api/users/list`

4. Filtrar la lista de usuarios por nombre  
   **URL:** `localhost:8080/api/users/filter`  
   **Parámetro:** `@Param firstName` -> Primer nombre mediante el cual filtraremos.


## Notas adicionales

### Conversión de archivos a formato Unix (LF)
Si estás trabajando en un sistema Windows y encuentras problemas al ejecutar los contenedores, puede deberse al formato de los finales de línea (`CRLF`) del archivo init-localstack.sh. Para convertir el archivo al formato Unix (`LF`), usa el siguiente comando:

```bash
dos2unix init-localstack.sh
```

Si no tienes dos2unix instalado, puedes hacerlo mediante WSL con el comando:
```bash
sudo apt install dos2unix
```


Puede identificar este problema si al utilizar los endpoints recibe este mensaje de error 

```bash
Error al procesar la solicitud: software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException: The specified queue does not exist. (Service: Sqs, Status Code: 400, Request ID: 4d553538-cd1c-4be0-823f-f82f23a6d5ae)
```

