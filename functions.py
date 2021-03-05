import spacy
import re
nlp = spacy.load('en_core_web_sm')#, disable=['parser', 'ner'])

"""Summary or Description of the Function

    Parameters:
    argument1 (int): Description of arg1

    Returns:
    int:Returning value

   """

#  Functions to load and write archives
def load_doc(filename: str):
  # Opening the file as read only
  file = open(filename, 'r')
  text = file.readlines() #file.read()
  file.close()
  return text

def write_doc(filename: str, text: str):
  # Opening the file as write only
  file = open(filename, 'w')
  text = file.write(text)
  file.close()


 
def word2int(writtenNumber: str) -> int:
  '''Function to process numbers in a sentence'''
  vocab = {
    "zero": 0,
    "one": 1,
    "two": 2,
    "three": 3,
    "four": 4,
    "five": 5,
    "six": 6,
    "seven": 7,
    "eight": 8,
    "nine": 9,
    "ten": 10,
  }
  try:
    if type(float(writtenNumber)) == float:
      return int(float(writtenNumber)) 
  except:
      writtenNumber = re.sub('[\W_]+', '', writtenNumber) # Limpiamos el string de caracteres no imprimibles
      return vocab[writtenNumber]




stopwords = nlp.Defaults.stop_words

def clean(list: ('List of strings')) -> list:
  '''Function to clean a list of words of stop words'''
  list_without_sw = [word for word in list if not word in stopwords]
  return list_without_sw


def clean_word(list: ('List of strings'), word: str) -> list:
  '''Function to delete a word from list of words'''
  list_without_sw = [item for item in list if item.lower() != word.lower()]
  return list_without_sw



def camel_case_split(str)-> list: 
  '''Splits camel case words and returns a list of words'''
  return re.findall(r'[A-Z](?:[a-z]+|[A-Z]*(?=[A-Z]|$))', str) 



def add_word_to_dict(dict: ("{key: str, value: list of words}"), key: str,  word : str):
  '''Adds a word to the list of words assigned to the key in the dictionary'''
  new_list = dict[key]
  new_list.append(word)
  dict[key] = new_list


def search_matches(dictionary, list) -> list :
  """Returns the list of words that has some match with some word of the list
  of words introduced as a parameter"""

  result = []

  for term in list:
    for item in dictionary: 
      for word in dictionary[item]:
        if term.lower() == word.lower() and term.lower():# != 'anomaly' and term.lower() != 'threat':
          result.append(item)

  return result



def counter(list: str) -> dict:
  """counts the repeated terms from a list of words and returns a dictionary with 
  the terms and repetitions of each term"""
  count = {}
  for item in list:
    if item in count:
      count[item] += 1
    else:
      count[item] = 1
  return count



def higher_frecuency_term(dict: ('{str: term, int: number of repetitions}')) -> str:
  """returns the most repeated term of the dictionary"""

  cont = 0
  hft = ''
  for term in dict:
     if dict[term] > cont:
       cont = dict[term]
       hft = term 
  return hft


def terms_dict(filename: str) -> dict:
  """Function to extract the terms in an archive and returns a dictionary of the
  terms splitted in theis main words"""
  
  terms = load_doc(filename)
  terms_list = []
  terms_dict = {}

  for line in terms:
    clean_line = re.sub('[\W_]+', '', line)
    terms_list.append(clean_line)
  
  for item in terms_list:
    terms_dict[item] = camel_case_split(item)
  
  return terms_dict



def extract_prob(sentence: str)-> int:
  """Returns the number of the value referred to the term probability in a
  sentence it can be written number or even decimal numbers but it will
  return an int""" 

  doc = nlp(str(sentence))
  prob_synonyms = ['probability','prob']
  num = [word2int(token.text) for token in doc if token.head.text.lower() in prob_synonyms and token.pos_ == "NUM"]
  return num[0]

def extract_impact(sentence: str)-> int:
  """Returns the number of the value referred to the term impact in a
  sentence it can be written number or even decimal numbers but it will
  return an int""" 

  doc = nlp(str(sentence))
  impact_synonyms = ['impactability','impact']
  num = [word2int(token.text) for token in doc if token.head.text.lower() in impact_synonyms and token.pos_ == "NUM"]
  return num[0]


def add_childs(word : spacy.tokens.token.Token, list : []) -> list:
  """Finds the word down in the dependency tree of a given word and returns
  the list with the whole syntagma"""
  
  if len([word.lefts]) > 0:
    for child in word.children:
      list.append(child.text)
      add_childs(child, list)


def extract_subject(sentence : str) -> list:
  """Function that finds the whole subject syntagma of a given sentence"""

  doc = nlp(str(sentence))
  subject = []
  for token in doc:
    if token.dep_ == 'ROOT' and token.pos_ == 'VERB':
      for child in token.children:
        if child.dep_ == 'nsubj':
          subject.append(child.text)
          add_childs(child, subject)
  return subject


def extract_dobject(sentence : str) -> list:
  """Function that finds the whole direct object syntagma of a given sentence"""

  doc = nlp(str(sentence))
  dobject = []
  for token in doc:
    if token.dep_ == 'ROOT' and token.pos_ == 'VERB':
      for child in token.children:
        if child.dep_ == 'dobj':
          dobject.append(child.text)
          add_childs(child, dobject)
  return dobject 


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
  # print(doc)
  # print(result)