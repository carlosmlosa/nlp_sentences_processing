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