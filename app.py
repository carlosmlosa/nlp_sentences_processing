from flask import Flask
from flask import render_template
from flask import request
from functions import write_json, read_json


'''No entiendo bien por qué, pero se ejecutan todos los archivos
sentence_processing. de principio a fin, y luego no importa las
funciones... o por lo menos no me deja ejecutarlas
quiza deba aprovechar para hacer el script discriminador de frases, 
total como le voy a pasar el tipo de frase en el json solo tiene qu
que mirarlo y ejecutar, con unos if será suficiente.'''

app = Flask(__name__)

@app.route('/')
def index():
    return "Funciona"

'''Será necesario pasar la oracion y el tipo en el json
hasta poder implementar el selector de funciones'''
@app.route('/input', methods=['POST','GET'])
def process_input():
    try:
        json_data = request.get_json(force=True) 
        sentences = json_data["sentence"]
        sentence_type = json_data["sentence_type"]
        # print(f"request jsondata es: {json_data}")
        write_json("./input/servidor.json", json_data)
        import sentenceTypeDetector #esto es una guarrada pero funciona así que ahí queda
        return sentences #read_json("./results/servidor.json")
    except:
        print("key error")
        return "key error"

@app.route('/output')
def serve_output():
    return read_json('./results/servidor.json')

    '''// // Ejemplo implementando el metodo POST:
async function postData(url = '', data = {}) {
  // Opciones por defecto estan marcadas con un *
  const response = await fetch(url, {headers: {"Content-Type": "application/json" },
    method: 'POST', // *GET, POST, PUT, DELETE, etc.
    body: JSON.stringify(data) // body data type must match "Content-Type" header
    });
  //return response.json(); // parses JSON response into native JavaScript objects
console.log(data)}
datos = {sentence: "Computers are exposed to a Device Lost Threat with prob 5.0 and impact 1.0", sentence_type: "process_asset_threat_impact_prob_sentence"}
postData('/input', datos);'''

