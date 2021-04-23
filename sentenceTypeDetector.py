from functions import *
from sentence_processing.anomaly_threat_sentences import process_anomaly_threat_sentence
from sentence_processing.asset_threat_impact_prob import process_asset_threat_impact_prob_sentence
from sentence_processing.asset_threats_sentences import process_asset_threats_sentence
from sentence_processing.threat_risk_sentence import process_threats_risks_sentence
import spacy

"""Este script debe leer un txt con muchas frases y agruparlas en 
tipos de frases, crear txt con las frases del mismo tipo
y llamar a las funciones procesadoras para que procesen estos txt"""

nlp = spacy.load('en_core_web_sm')#, disable=['parser', 'ner'])

sentences = load_doc("./input/sentences.txt")


def save_sentences_txt(filename: str,set: set):
  text = ""
  for item in set:
    text += item 

  write_doc(filename,text)

asset_threat_impact_prob_sentence = set()
asset_threat_sentence = set()
threat_risk_sentence = set()
anomaly_threat_sentence = set()

for sentence in sentences:
  doc = nlp(sentence)
  for token in doc:
    if token.dep_ == "nsubjpass" or token.dep_ == "auxpass":
      asset_threat_impact_prob_sentence.add(sentence)
      save_sentences_txt("./input/asset_threat_impact_prob.txt",asset_threat_impact_prob_sentence)
      # process_txt("./input/asset_threat_impact_prob.txt",process_asset_threat_impact_prob_sentence,{})
      break
    if token.dep_ == "ROOT" and token.lemma_ == "threaten":
      print("entra en el verbo threaten")
      asset_threat_sentence.add(sentence)
      save_sentences_txt("./input/asset_threats_sentence.txt",asset_threat_sentence)
      # process_txt("./input/asset_threats_sentence.txt",process_asset_threats_sentence,{})
      break
    if token.lemma_ == "impact" or token.lemma_ == "probability":
      anomaly_threat_sentence.add(sentence)
      save_sentences_txt("./input/anomaly_threat.txt",anomaly_threat_sentence)
      # process_txt("./input/anomaly_threat.txt",process_anomaly_threat_sentence,{})
      break
    else:
      threat_risk_sentence.add(sentence)
      save_sentences_txt("./input/threat_risk_sentence.txt",threat_risk_sentence)
      # process_txt("./input/threat_risk_sentence.txt",process_threats_risks_sentence,{})
      break