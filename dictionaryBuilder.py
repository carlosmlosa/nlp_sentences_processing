
from functions import terms_dict
from functions import add_word_to_dict
from functions import write_json

#threats_dict
#--------------------------------------------------------------------------------
threats_file = './files/threats.txt'
threats_dict = terms_dict(threats_file)

# Añadimos el termino 'intentional' a todos los términos que tengan la palabra 
# 'deliberated' porque 'deliberated' da problemas y sugerimos evitar esa palabra
for k,v in threats_dict.items():
  if 'Deliberated' in threats_dict[k]:
    add_word_to_dict(threats_dict,k, 'intentional')

# añadimos las palabras para el acrónimo HW
for k,v in threats_dict.items():
  if 'HW' in threats_dict[k]:
    add_word_to_dict(threats_dict,k, 'hardware')

# añadimos las palabras para el acrónimo SW
for k,v in threats_dict.items():
  if 'SW' in threats_dict[k]:
    add_word_to_dict(threats_dict,k, 'software')

#print(threats_dict)
write_json("./dictionaries/threats_dict.json",threats_dict)

#anomalies_dict
#--------------------------------------------------------------------------------
anomalies_file = './files/anomalies.txt'
anomalies_dict = terms_dict(anomalies_file)
# Es necesario modificar algunas por que no detecta bien las palabras como WiFi
add_word_to_dict(anomalies_dict, 'WiFi_Sensor_Anomaly', 'WiFi')
write_json("./dictionaries/anomalies_dict.json",anomalies_dict)

#assets_dict
#--------------------------------------------------------------------------------
assets_file = './files/assets.txt'
assets_dict = terms_dict(assets_file)
write_json("./dictionaries/assets_dict.json",assets_dict)

#risks_dict
#--------------------------------------------------------------------------------
risks_file = './files/risks.txt'
risks_dict = terms_dict(risks_file)
write_json("./dictionaries/risks_dict.json",risks_dict)
