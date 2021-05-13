package tfg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.topbraid.spin.util.JenaUtil;

public class ThreatRisk {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
		
		// Load main file
				Model baseModel = ModelFactory.createDefaultModel();
				baseModel.read("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/recursos/Protege-5.5.0/PLICA_prueba_1.owl"); 
				
				// Create OntModel with imports
				OntModel ontModel = JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,baseModel);
				
				//Cargar el JSON y sacar datos a variables
				JSONParser parser = new JSONParser();
//				Object obj = parser.parse(new FileReader("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/python/results.json"));
				Object obj = parser.parse(new FileReader("C:/Users/charl/cosas_CHARLIE/DESKTOP_LOCAL/TFG/nlp_sentences_processing/results/servidor.json"));
		        JSONObject jsonObject = (JSONObject)obj;
//		        System.out.println(jsonObject);
		        

		        JSONArray resultsArr = (JSONArray)jsonObject.get("result");
		        System.out.println(resultsArr);
		        String sentenceType = (String)jsonObject.get("sentenceType"); 
		        JSONObject terms =  (JSONObject) resultsArr.get(0);
		        //Array nombre risk
		        JSONArray riesgos = (JSONArray)terms.get("riskType");
				// string nombre amenaza
		        String amenaza = (String)terms.get("threatType");
		       
		        System.out.println(terms);
		        System.out.println(riesgos);
		        System.out.println(amenaza);
		    	
					
				String co = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#";
				String drm = "http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#";
				String ona = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#";
				
				
		        for(int i=0; i<riesgos.size();i++) {
		        	String riesgo = (String) riesgos.get(i);
		        	String query ="PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
	                            + "PREFIX cyberthreat_DRM: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#>\n"
	                            + "PREFIX cyberthreat_STIX: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_STIX#>\n"
	                            + "PREFIX cyberthreat_ONA: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#>\n"
	                            + "PREFIX cibersituational-ontology: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#>\n"
	                            + "CONSTRUCT {\n"
	                            + "?t cibersituational-ontology:generates ?r.\n"
	                            + "?r cibersituational-ontology:isGeneratedBy ?t.\n"
	                            + "?r a cyberthreat_DRM:Risk.\n"
	                            + "}\n"
	                            + "WHERE{\n"
	                            + "?t a cyberthreat_DRM:"+ amenaza + ".\n"
	                            + "?r a cyberthreat_DRM:" + riesgo + ".}";
		    				
    				System.out.println(query);

    				//La ejecución del construct será similar al SELECT, pero variando el metodo execSelect por el que corresponda, y no será necesario recorrer resultados.
    				Query constructQuery= QueryFactory.create(query);
    				QueryExecution constructQueryExec=QueryExecutionFactory.create(constructQuery, ontModel);
    				Model create =constructQueryExec.execConstruct(); 
    				ontModel.add(create);
		        	
	        }
				


				
				

				 //Para guardar (en otro .owl, por si acaso se guarda algun dato que no sea correcto, no perder el original)
				 try {
					FileOutputStream oFile = new FileOutputStream("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/recursos/Protege-5.5.0/PLICA_prueba_1.owl", false);
					ontModel.write(oFile);
				} catch (Error e) { System.out.println(e);}


	}

}
