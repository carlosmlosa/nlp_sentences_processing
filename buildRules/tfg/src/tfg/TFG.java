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

import org.json.simple.parser.ParseException;
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

public class TFG {
	/**
	 * 
	 */
	/**
	 * @author Carlos Muñoz Losa
	 *
	 */
	
	public static void createAnomalyThreatRule(JSONArray resultsArr, OntModel ontModel) throws FileNotFoundException {
		System.out.println("se llama al metodo createAnomalyThreatRule");
        for(int j=0; j<resultsArr.size(); j++)
        {
        	JSONObject terms =  (JSONObject) resultsArr.get(j);
        	System.out.println(terms);
        	
        	//probando subir versión a github
        	//string nombre anomalia
        	String anomalia = (String)terms.get("anomalyType");
    		// string nombre amenaza
            String amenaza = (String)terms.get("threatType");
            JSONArray impactArray = (JSONArray)terms.get("impact");
            //Long impact
            Long impact = impactArray.size() > 0 ? (Long) impactArray.get(0) : 1; // en caso de que se haya detectado mal se asigna 1
            JSONArray probArray = (JSONArray)terms.get("prob");
            //Long impact
            Long prob = probArray.size() > 0?(Long) probArray.get(0):1; // en caso de que se haya detectado mal se asigna 1
            System.out.println(terms);
        	System.out.println("anomalia:" + anomalia);
            System.out.println("amenaza:" + amenaza);
            System.out.println("prob:" + prob);
            System.out.println("impact:" + impact);


	        
	        String co = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#";
			String ona = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#";
			String drm = "http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#";
			
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
					+ "?w a cyberthreat_ONA:" + anomalia + ".\n"  //aqui ponía anomalyType lo cambio por anomalia
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
				System.out.println("crea amenaza"+i);
				//Asocia anomalia y amenaza
				t.addProperty(isGenBy, ontModel.getIndividual(ona + name.get(i)));
				//Guarda el individuo en la clase de esa amenaza
				t.addOntClass(ontModel.getOntClass(drm+amenaza));
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
				System.out.println("Finalizado");
			} catch (Error e) { System.out.println(e);}
        }
	}
	
	
	
	public static void createThreatRisksRule(JSONArray resultsArr, OntModel ontModel) throws FileNotFoundException  {
		
		for(int j=0; j<resultsArr.size(); j++)
        {
        	JSONObject terms =  (JSONObject) resultsArr.get(j);
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
	
	
	 public static void createAssetThreatImpactProbRule(JSONArray resultsArr, OntModel ontModel) throws FileNotFoundException {
	    	for(int j=0; j<resultsArr.size(); j++)
	        {
	    		JSONObject terms =  (JSONObject) resultsArr.get(0);
		        //Array nombre risk
		        String activo = (String)terms.get("asset");
				// string nombre amenaza
		        String amenaza = (String)terms.get("threatType");
		        JSONArray impactArray = (JSONArray)terms.get("impact");
		        Long impact = impactArray.size() > 0 ? (Long) impactArray.get(0) : 1; // en caso de que se haya detectado mal se asigna 1
		        JSONArray probArray = (JSONArray)terms.get("prob");
		        Long prob = probArray.size() > 0?(Long) probArray.get(0):1; // en caso de que se haya detectado mal se asigna 1
		       
		        System.out.println(terms);
		        System.out.println(activo);
		        System.out.println(amenaza);
		        System.out.println(impact);
		        System.out.println(prob);
		    	
					
				String co = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#";
				String drm = "http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#";
				String ona = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#";
				
				OntClass threat = ontModel.getOntClass(drm + "Threat");
				DatatypeProperty type = ontModel.createDatatypeProperty( co + "type" );
				ObjectProperty threatens = ontModel.createObjectProperty( drm + "threatens" );
				
				
				
				String select = "PREFIX cyberthreat_DRM: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#>\n"
						+ "PREFIX cyberthreat_STIX: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_STIX#>\n"
						+ "PREFIX cyberthreat_ONA: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#>\n"
						+ "PREFIX cibersituational-ontology: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#>\n"
						+ "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
						+ "SELECT ?w \n"
						+ "WHERE{\n"
						+ "?w a cyberthreat_DRM:" + activo + ".\n" 
						+ "}\n";
				
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
					Individual t = ontModel.createIndividual( drm + amenaza + "_" + i, threat);
					//Asocia activo y amenaza
					t.addProperty(threatens, ontModel.getIndividual(co + name.get(i)));
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
				        " ?activo a cyberthreat_DRM:"+ activo + ".\n" +
				        	"?amenaza a cyberthreat_DRM:"+ amenaza + ".\n" +
				        	"?amenaza cyberthreat_DRM:threatens ?activo .\n" +
				        "}\n";
				
				System.out.println(construct);

				//La ejecución del construct será similar al SELECT, pero variando el metodo execSelect por el que corresponda, y no será necesario recorrer resultados.
				Query constructQuery= QueryFactory.create(construct);
				QueryExecution constructQueryExec=QueryExecutionFactory.create(constructQuery, ontModel);
				Model create =constructQueryExec.execConstruct(); 
				ontModel.add(create);
	        }
	    	
	    	try {
				FileOutputStream oFile = new FileOutputStream("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/recursos/Protege-5.5.0/PLICA_prueba_1.owl", false);
				ontModel.write(oFile);
			} catch (Error e) { System.out.println(e);}
	    	
	    }
	 
	 public static void createAssetsThreatRule(JSONArray resultsArr, OntModel ontModel) throws FileNotFoundException{
		 JSONObject terms =  (JSONObject) resultsArr.get(0);
	        //Array nombre risk
	        String activo = (String)terms.get("asset");
			// string nombre amenaza
	        JSONArray amenazas = (JSONArray)terms.get("threatType");
	        JSONArray impactArray = (JSONArray)terms.get("impact");
	        Long impact = impactArray.size() > 0 ? (Long) impactArray.get(0) : 1; // en caso de que se haya detectado mal se asigna 1
	        JSONArray probArray = (JSONArray)terms.get("prob");
	        Long prob = probArray.size() > 0?(Long) probArray.get(0):1; // en caso de que se haya detectado mal se asigna 1
	       
	        System.out.println(terms);
	        System.out.println(activo);
	        System.out.println(amenazas);
	        System.out.println(impact);
	        System.out.println(prob);
	    	
				
			String co = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#";
			String drm = "http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#";
			String ona = "http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#";
			
			OntClass threat = ontModel.getOntClass(drm + "Threat");
			DatatypeProperty type = ontModel.createDatatypeProperty( co + "type" );
			ObjectProperty threatens = ontModel.createObjectProperty( drm + "threatens" );
			
			
			for(int j=0; j<amenazas.size();j++) {
	        	String amenaza = (String) amenazas.get(j);
	        	
				String select = "PREFIX cyberthreat_DRM: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_DRM#>\n"
				+ "PREFIX cyberthreat_STIX: <http://www.semanticweb.org/upm/ontologies/2019/11/cyberthreat_STIX#>\n"
				+ "PREFIX cyberthreat_ONA: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cyberthreat_ONA#>\n"
				+ "PREFIX cibersituational-ontology: <http://www.semanticweb.org/paulagarcia/ontologies/2020/2/cibersituational-ontology#>\n"
				+ "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
				+ "SELECT ?w \n"
				+ "WHERE{\n"
				+ "?w a cyberthreat_DRM:" + activo + ".\n" 
				+ "}\n";
		
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
					Individual t = ontModel.createIndividual( drm + amenaza + "_" + i, threat);
					//Asocia activo y amenaza
					t.addProperty(threatens, ontModel.getIndividual(co + name.get(i)));
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
				        " ?activo a cyberthreat_DRM:"+ activo + ".\n" +
				        	"?amenaza a cyberthreat_DRM:"+ amenaza + ".\n" +
				        	"?amenaza cyberthreat_DRM:threatens ?activo .\n" +
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
	

		public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

			// Load main file
			Model baseModel = ModelFactory.createDefaultModel();
			baseModel.read("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/recursos/Protege-5.5.0/PLICA_prueba_1.owl"); 
			
			// Create OntModel with imports
			OntModel ontModel = JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,baseModel);
			
			//Cargar el JSON y sacar datos a variables
			JSONParser parser = new JSONParser();
//			Object obj = parser.parse(new FileReader("C:/Users/charl/OneDrive - Universidad Politécnica de Madrid/TELECO4/TFG/python/results.json"));
			Object obj = parser.parse(new FileReader("C:/Users/charl/cosas_CHARLIE/DESKTOP_LOCAL/TFG/nlp_sentences_processing/results/servidor.json"));
	        JSONObject jsonObject = (JSONObject)obj;
//	        System.out.println(jsonObject);
	        
	        JSONArray resultsArr = (JSONArray)jsonObject.get("result");
	        String sentenceType = (String)jsonObject.get("sentenceType");  
	        
//	        Insertar aquí todos los tipos de reglas para ejecutar las diferentes funciones de creación de reglas
	        if (sentenceType.equals("process_anomaly_threat_sentence")) {
	        	createAnomalyThreatRule(resultsArr, ontModel);
	        }else if(sentenceType.equals("process_asset_threat_impact_prob_sentence")) {
	        	createAssetThreatImpactProbRule(resultsArr, ontModel);
	        }else if(sentenceType.equals("process_asset_threats_sentence")) {
	        	createAssetsThreatRule(resultsArr, ontModel);
	        }else if(sentenceType.equals("process_threats_risks_sentence")) {
	        	createThreatRisksRule(resultsArr, ontModel);
	        }
	        

	        
//	        System.out.println(jsonObject);
//	        System.out.println(resultsArr);
	        
			
			
		}
		
}
