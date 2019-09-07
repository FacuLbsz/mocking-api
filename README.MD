# Microservicio de Mocks

### Requisitos
```
Maven
Java / Jdk 8
MongoDB
```

### Para inicar localmente

Modificar application-development.properties indicando la base de datos local:

```
# Database name.
spring.data.mongodb.database=${DATA_BASE_NAME}
# Mongo server host.
spring.data.mongodb.host=${DATA_BASE_HOST}
# Mongo server port.
spring.data.mongodb.port=${DATA_BASE_PORT}
```

Correr localmente en cmd indicando el puerto:
```
mvn spring-boot:run -Dspring.profiles.active=development -Dserver.port=8001
```


### Swagger para administrar mocks  

http://localhost:8001/swagger-ui.html

### Swagger para probar mocks

http://localhost:8001/swagger/mocks

### Ejemplos para crear mocks

El atributo "response" de un case, refiere a la respuesta que dara el servicio para ese caso configurado.
Esta respuesta debe ser una sola linea valida para que el json del mock/case sea valido.
Para lo cual se puede usar el endpoint:
Via POST:
http://localhost:8010/stringify
Via Swagger:
http://localhost:8010/swagger-ui.html#!/stringify/stringifyUsingPOST

-- Crear mock para llamados POST:
- Consume/produce XML:
    ```json
    {
    	"description" : "Mock de servicio para agregar mascotas a personas",
    	"method" : "POST",
    	"url" : "persona/{id}/mascotas",
    	"consume" : "XML",
    	"cases" : [{
    		"xml_validations": [{
    						"value" : "<raza>salchicha</raza>"
    					}],
    		"url_params_validations" : [{
    							"param_name" : "id",
    							"value" : "5",
    							"any" : "false"
    							}],
    		"response" : "<resultado>OK</resultado>",
    		"response_status_code" : "200"
    				},{
    		"xml_validations": [{
    						"value" : "<raza>calle</raza>"
    					}],
    		"url_params_validations" : [{
    							"param_name" : "id",
    							"any" : "true"
    						}],
    		"response" : "<resultado>ERROR</resultado>",
    		"response_status_code" : "500"
	}]}
    ```
	
- Consume/produce JSON:
    ```json
    {
    	"description" : "Mock de servicio para agregar mascotas a personas",
    	"method" : "POST",
    	"url" : "persona/{id}/mascotas",
    	"consume" : "JSON",
    	"cases" : [{
    		"json_validations": [{
    						"field" : "perro.raza.nombre",
    						"value" : "salchicha"
    					}],
    		"url_params_validations" : [{
    							"param_name" : "id",
    							"value" : "5",
    							"any" : "false"
    							}],
    		"response" : "{\"resultado\":\"OK\"}",
    		"response_status_code" : "201"
    				},{
    		"json_validations": [{
    						"field" : "perro.raza.nombre",
    						"value" : "calle"
    					}],
    		"url_params_validations" : [{
    							"param_name" : "id",
    							"any" : "true"
    						}],
    		"response" : "{\"resultado\":\"ERROR\"}",
    		"response_status_code" : "500"
	}]}
    ```
	
-- Crear mock para llamados GET:
- Consume/produce JSON: 
    ```json
    {
    	"description" : "Mock de servicio para obtener las mascotas de personas",
    	"method" : "GET",
    	"url" : "persona/{id}/mascotas",
    	"consume" : "JSON",
    	"query_params" : [{
    		"param_name" : "raza"
    	}],
    	"path_variables" : [{
    		"name" : "id"
    	}],
    	"cases" : [{
    		"query_params_validations" : [{
    			"param_name" : "raza",
    			"value" : "salchicha"
    		}],
    		"url_params_validations" : [{
    			"param_name" : "id",
    			"value" : "5"
    		}],					
    		"response" : "[{\"nombre\":\"nacho\"}]",
    		"response_status_code" : "200"
    	},
    	{
    		"query_params_validations" : [{
    			"param_name" : "raza",
    			"value" : "labrador"
    		}],
    		"url_params_validations" : [{
    			"param_name" : "id",
    			"value" : "5"
    		}],					
    		"response" : "[]",
    		"response_status_code" : "200"
	}]}
    ```
	
- Consume/produce XML: 
    ```json
    {
    	"description" : "Mock de servicio para obtener las mascotas de personas",
    	"method" : "GET",
    	"url" : "persona/{id}/mascotas",
    	"consume" : "XML",
    	"query_params" : [{
    		"param_name" : "raza"
    	}],
    	"path_variables" : [{
    		"name" : "id"
    	}],
    	"cases" : [{
    		"query_params_validations" : [{
    			"param_name" : "raza",
    			"value" : "salchicha"
    		}],
    		"url_params_validations" : [{
    			"param_name" : "id",
    			"value" : "5"
    		}],					
    		"response" : "<mascotas><mascota><nombre>nacho</nombre></mascota></mascotas>",
    		"response_status_code" : "200"
    	},
    	{
    		"query_params_validations" : [{
    			"param_name" : "raza",
    			"value" : "labrador"
    		}],
    		"url_params_validations" : [{
    			"param_name" : "id",
    			"value" : "5"
    		}],					
    		"response" : "<mascotas></mascotas>",
    		"response_status_code" : "200"
	}]}
    ```


Autor: Facundo Vazquez