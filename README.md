<img  align="left" width="150" style="float: left;" src="https://www.upm.es/sfs/Rectorado/Gabinete%20del%20Rector/Logos/UPM/CEI/LOGOTIPO%20leyenda%20color%20JPG%20p.png">
<br></br><br></br>

# Elaboración de reglas de una ontología mediante procesamiento del lenguaje natural

Este repositorio forma parte de mi trabajo de fin de grado. Consiste en procesar frases de lenguaje natural para extraer los términos y la información necesaria para poder construir reglas de inferencia de conocimiento para una ontología de la información.

## Estructura
Tiene tres partes principales
 * **Frontend** hecho con react
 * **Procesamiento** Distribuido en los archivos **functions.py** ,**dictionaryBuilder.py**, **sentenceTypeDetector.py** y en el directorio **/sentence_processing**
 * **JARFiles** donde se crean las regla de la ontología

Las tres partes se conectan por medio del servidor Flask definido en el **app.py**, el frontend se comunica mediante peticiones HTTP 

## Comenzando 🚀

Es necesario clonar el proyecto con `git clone`


### Pre-requisitos 📋

Python v3.9
Spacy v3
Java
Eclipse EE
Flask
npm/yarn

Spacy se puede instalar con el comando
```
pip install spacy
```

### Instalación 🔧

Instalar las dependencias de node

_Desde el directorio /frontend_

`
npm install
`


## Ejecutando las pruebas ⚙️

Para poder probar el proyecto es necesario

_Lanzar servidor Flask_

`
flask run
`

_Lanzar frontend react, desde el directorio /frontend_


`
npm start
`


### Y las pruebas de estilo de codificación ⌨️

_Explica que verifican estas pruebas y por qué_

```
Da un ejemplo
```

## Autores ✒️


* **Carlos Muñoz Losa** - *Trabajo Inicial* - [carlosmlosa](https://github.com/carlosmlosa)

