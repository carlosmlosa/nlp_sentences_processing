import os, sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
from functions import *
import spacy 


threats_file = './dictionaries/threats_dict.json'
threats_dict = read_json(threats_file)

assets_file = './dictionaries/assets_dict.json'
assets_dict = read_json(assets_file)

def process_asset_threat_impact_prob_sentence(sentence: str, result: {}):
  """Function that process a sentences finding the terms:
  asset, threatType, prob and impact"""
  sentence = sentence.replace(",",".") # Para admitir decimales con "," o "."
  doc = nlp(str(sentence)) 
  
  asset = extract_passive_subject(sentence)      
  asset = clean(asset)
  asset_matches = search_matches(assets_dict, asset)
  asset_count = counter(asset_matches)
  asset = higher_frecuency_term(asset_count)
  result["asset"] = asset


  threat = extract_threat_passive(sentence)
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
# result = {}
# process_txt("./input/asset_threat_impact_prob.json", process_asset_threat_impact_prob_sentence,result)



