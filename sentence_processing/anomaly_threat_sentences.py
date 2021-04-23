import os, sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
from functions import *
import spacy 


threats_file = './dictionaries/threats_dict.json'
threats_dict = read_json(threats_file)

anomalies_file = './dictionaries/anomalies_dict.json'
anomalies_dict = read_json(anomalies_file)

def process_anomaly_threat_sentence(sentence: str, result: {}):
  """Function that process a sentence for finding the terms prob, anomalyType, 
  threatType and impact""" 
  
  sentence = sentence.replace(",",".") # Para admitir decimales con "," o "."

  doc = nlp(str(sentence))

  anomaly = extract_subject(sentence)
  anomaly = clean(anomaly)
  anomaly_matches = search_matches(anomalies_dict, anomaly)
  anomaly_count = counter(anomaly_matches)
  anomaly = higher_frecuency_term(anomaly_count)
  result["anomalyType"] = anomaly

  threat = extract_dobject(sentence)
  threat = clean(threat)
  threat_matches = search_matches(threats_dict, threat)  
  threat_count = counter(threat_matches)  
  threat = higher_frecuency_term(threat_count)  
  result["threatType"] = threat

  result["prob"] = extract_prob(sentence)
  result["impact"] = extract_impact(sentence)
    
  return result



'''Esto es solo para comprobar que funciona, en realidad con la función
process_txt inyectandole los parámetros correctos debería funcionar
así podemos reutilizar las funciones y los archivos como y cuando queramos'''
result = {}
process_txt("./input/anomaly_threat.txt", process_anomaly_threat_sentence,{})



