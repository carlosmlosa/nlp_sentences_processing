import os, sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
from functions import *
import spacy 

nlp = spacy.load('en_core_web_sm')

threats_file = './dictionaries/threats_dict.json'
threats_dict = read_json(threats_file)

risks_file = './dictionaries/risks_dict.json'
risks_dict = read_json(risks_file)


def process_threats_risks_sentence(sentence: str, result: {}):
    """Function that process a sentence for finding the terms prob, anomalyType, 
    threatType and impact""" 

    # sentence = sentence.replace(",",".") # Para admitir decimales con "," o "."

    doc = nlp(str(sentence))

    raw_risks = extract_dobject(sentence)
    # print(raw_risks)
    risks= []
    aux = []
    for element in raw_risks:
        if element == "," or element == "and":
            risks.append(aux)
            aux = []
        elif element == raw_risks[-1]:
            risks.append(aux)
        else:
            aux.append(element)

    
    risksList = []
    for risk in risks:
        risk = clean(risk)
        risk_matches = search_matches(risks_dict, risk)
        risk_count = counter(risk_matches)
        risk = higher_frecuency_term(risk_count)
        risksList.append(risk)
        result["riskType"] = risksList

    threat = extract_subject(sentence)
    threat = clean(threat)
    threat_matches = search_matches(threats_dict, threat)  
    threat_count = counter(threat_matches)  
    threat = higher_frecuency_term(threat_count)  
    result["threatType"] = threat

    # result["prob"] = extract_prob(sentence)
    # result["impact"] = extract_impact(sentence)

    return result
    # print(doc)
    # print(result)

'''Esto es solo para comprobar que funciona, en realidad con la función
process_txt inyectandole los parámetros correctos debería funcionar
así podemos reutilizar las funciones y los archivos como y cuando queramos'''
# result = {}
# process_txt("./input/threat_risk.json", process_threats_risks_sentence,{})


