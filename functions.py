# -*- coding: utf-8 -*-
"""functions.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1BWsu80kDWK2WW-wqkKjC6Y75Un6vtvMF
"""

import spacy
import re
import json
import os

nlp = spacy.load('en_core_web_sm')#, disable=['parser', 'ner'])
# es_core_news_sm para español


#  Functions to load and write archives
def load_doc(filename: str):
  # Opening the file as read only
  if not os.path.exists(filename):
    file = open(filename, 'w')
    text = file.write("") #file.read()
    file.close()

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
  terms splitted in theis main words and its lemmas"""
  
  terms = load_doc(filename)
  terms_list = []
  terms_dict = {}

  terms_list += [re.sub('[\W_]+', '', line) for line in terms]

  for item in terms_list:
    terms_dict[item] = camel_case_split(item)
    doc = nlp(' '.join(terms_dict[item]))
    for token in doc:
      if token.lemma_ not in terms_dict[item]:
        terms_dict[item].append(token.lemma_) 
  
  return terms_dict



def extract_prob(sentence: str)-> int:
  """Returns the number of the value referred to the term probability in a
  sentence it can be written number or even decimal numbers but it will
  return an int list""" 

  doc = nlp(str(sentence))
  num = 0
  prob_synonyms = ['probability','prob']
  num = [word2int(token.text) for token in doc if token.head.text.lower() in prob_synonyms and token.pos_ == "NUM"]
  return num

def extract_impact(sentence: str)-> int:
  """Returns the number of the value referred to the term impact in a
  sentence it can be written number or even decimal numbers but it will
  return an int list""" 
  num = 0
  doc = nlp(str(sentence))
  impact_synonyms = ['impactability','impact']
  num = [word2int(token.text) for token in doc if token.head.text.lower() in impact_synonyms and token.pos_ == "NUM"]
  return num


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
          subject.append(child.lemma_) #child.text
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
          dobject.append(child.lemma_)
          add_childs(child, dobject)
  return dobject 

def extract_passive_subject(sentence : str) -> list:
  """Function that finds the whole passive 
  subject syntagma of a given sentence"""

  doc = nlp(str(sentence))
  subject = []
  for token in doc:
    if token.dep_ == 'ROOT' and token.pos_ == 'VERB':
      for child in token.children:
        if child.dep_ == 'nsubjpass':
          subject.append(child.lemma_)
          add_childs(child, subject)
  return subject

def extract_threat_passive(sentence: str)-> list:
  doc = nlp(str(sentence))
  threat = []
  for token in doc:
    if token.dep_ == 'ROOT' and token.pos_ == 'VERB':
      for child in token.children:
        if child.dep_ == 'prep':
          threat.append(child.lemma_)
          add_childs(child, threat)
  return threat

def write_json(filename: str, dict):
  """Function that creates a json file from a dict object"""

  with open(filename, 'w') as outfile:
    json.dump(dict, outfile)

def read_json(filename: str):
  """Function that creates a dict object  from a json file"""

  with open(filename) as file:
    data = json.load(file)
  return data



def process_txt(filename: str, function, dict):
  """Function that creates a json file from a file with sentences, function needs 
  to be specified in order to process the sentences, and the output dictionary 
  has to be specified too"""
  input = read_json(filename)
  sentence = input["sentence"] #sentences
  resultList = []

  # for sentence in sentences:
  #   result = {}
  #   dict = {}
  #   result = function(sentence, dict)
  #   resultList.append(result)
  dict = {}
  result = function(sentence, dict)
  resultList.append(result)
  
  results = {"result": resultList}
  filename = filename.split("input")[0] +"results" + filename.split("input")[1]
  # filename_out = filename.split(".txt")[0] + "_sentence" + ".json"
  write_json(filename,results)