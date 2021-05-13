package tfg;
import org.topbraid.spin.util.JenaUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;



import org.apache.http.HttpHost;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.function.FunctionRegistry;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;


public class Prueba {
	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
		
		
		
        
		// Load main file
		Model baseModel = ModelFactory.createDefaultModel();
		baseModel.read("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/recursos/Protege-5.5.0/PLICA_prueba.owl"); 
		
		// Create OntModel with imports
		OntModel ontModel = JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,baseModel);
		
		//Cargar el JSON y sacar datos a variables
		JSONParser parser = new JSONParser();
//		Object obj = parser.parse(new FileReader("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/python/results.json"));
		Object obj = parser.parse(new FileReader("C:/Users/charl/cosas_CHARLIE/DESKTOP_LOCAL/TFG/nlp_sentences_processing/results/servidor.json"));
        JSONObject jsonObject = (JSONObject)obj;
//        System.out.println(jsonObject);
        

        JSONArray resultsArr = (JSONArray)jsonObject.get("result");
        String sentenceType = (String)jsonObject.get("sentenceType"); 
        JSONObject terms =  (JSONObject) resultsArr.get(0);
      //string nombre anomalia
        String anomalia = (String)terms.get("anomalyType");
		// string nombre amenaza
        String amenaza = (String)terms.get("threatType");
        JSONArray impactArray = (JSONArray)terms.get("impact");
        Long impact = impactArray.size() > 0 ? (Long) impactArray.get(0) : 1; // en caso de que se haya detectado mal se asigna 1
        JSONArray probArray = (JSONArray)terms.get("prob");
        Long prob = probArray.size() > 0?(Long) probArray.get(0):1; // en caso de que se haya detectado mal se asigna 1
        System.out.println(terms);
    	System.out.println("anomalia:" + anomalia);
        System.out.println("amenaza:" + amenaza);
        System.out.println("prob:" + prob);
        System.out.println("impact:" + impact);
			
		String co = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#";
		String drm = "http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#";
		String ona = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#";
		
		OntClass threat = ontModel.getOntClass(drm + "Threat");
		DatatypeProperty type = ontModel.createDatatypeProperty( co + "type" );
		ObjectProperty isGenBy = ontModel.createObjectProperty( co + "isGeneratedBy" );
		
		String select = "PREFIX cyberthreat_DRM: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#>\n"
				+ "PREFIX cyberthreat_STIX: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_STIX#>\n"
				+ "PREFIX cyberthreat_ONA: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#>\n"
				+ "PREFIX cibersituational-ontology: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#>\n"
				+ "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
				+ "SELECT ?w \n"
				+ "WHERE{\n"
				+ "?w a cyberthreat_ONA:" + anomalia + ".\n" //aqui ponía anomalyType lo cambio por anomalia
				+ "?w cibersituational-ontology:related_sv ?sv.}\n";
		
		Query q= QueryFactory.create(select);
	    QueryExecution qexec=QueryExecutionFactory.create(q, ontModel);
	    ResultSet sel = qexec.execSelect();
	    //Array para guardar las anomalias de ese tipo
	    ArrayList<String> name = new ArrayList<String>();
	    //Recorrer los resultados del select
	    for (; sel.hasNext();) {
	        name.add(sel.next().get("w").toString().split("#")[1]);
	    }
	    //Numero de anomalias de ese tipo que existen
	    int n = name.size();
	    System.out.println(name);
	    
	  //Crea las amenazas necesarias
	    for (int i = 0; i<n; i++) {
	    	// Crea individuo de amenaza con el nombre guardado y un contador (i)
			Individual t = ontModel.createIndividual( co + amenaza +"_" + i, threat);
			//Asocia anomalia y amenaza
			t.addProperty(isGenBy, ontModel.getIndividual(ona + name.get(i)));
			//Guarda el individuo en la clase de esa amenaza
			t.addOntClass(ontModel.getOntClass(drm+amenaza));
			System.out.println("crea amenaza: "+ amenaza +"_" + i);
	    }
		
		String construct = "PREFIX cyberthreat_DRM: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#>\n"
		        +"PREFIX cyberthreat_STIX: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_STIX#>\n"
		        + "PREFIX cyberthreat_ONA: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#>\n"
		        + "PREFIX cibersituational-ontology: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#>\n"
		        + "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n" + "\n" +
		        "CONSTRUCT {?amenaza cibersituational-ontology:impact " + impact + ". \n" +
		        "?amenaza cibersituational-ontology:probability " + prob + "}  \n" +
		        "WHERE{\n" +
		        " ?anomalia a cyberthreat_ONA:"+ anomalia + ".\n" +
		        	"?amenaza a cyberthreat_DRM:"+ amenaza + ".\n" +
		        	"?amenaza cibersituational-ontology:isGeneratedBy ?anomalia .\n" +
		        "}\n";
		
		System.out.println(construct);

		//La ejecución del construct será similar al SELECT, pero variando el metodo execSelect por el que corresponda, y no será necesario recorrer resultados.
		Query constructQuery= QueryFactory.create(construct);
		QueryExecution constructQueryExec=QueryExecutionFactory.create(constructQuery, ontModel);
		Model create =constructQueryExec.execConstruct(); 
		ontModel.add(create);

		 //Para guardar (en otro .owl, por si acaso se guarda algun dato que no sea correcto, no perder el original)
		 try {
			FileOutputStream oFile = new FileOutputStream("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/recursos/Protege-5.5.0/PLICA_prueba_1.owl", false);
			ontModel.write(oFile);
		} catch (Error e) { System.out.println(e);}

		
	}
}
