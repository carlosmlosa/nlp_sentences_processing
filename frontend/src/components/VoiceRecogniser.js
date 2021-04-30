import "./VoiceRecogniser.css";
import React, { Component } from "react";
import Term from "./Term.js";
import SentenceInput from "./SentenceInput.js";
import SpeechRecognition, {
  useSpeechRecognition,
} from "react-speech-recognition";

let text = "";
let terms = new Map();
terms = {
  asset: ["Processes", "Computers"],
  threatType: "PrivilegeEscalation",
  prob: [3],
  impact: [2],
};
const handleChange = () => {
  text = document.getElementsByClassName("result")[0].value;
  console.log(text);
};

const sendSentence = async () => {
  const type = document.getElementsByClassName("select")[0].value;
  let datos = {};
  if (type === "anomaly_threat_sentences") {
    datos = {
      sentence: text,
      // "wifi sensor generates privilege escalation with probability 2 and impact 5",
      sentence_type: "process_anomaly_threat_sentence",
    };
  }
  if (type === "asset_threat_impact_prob") {
    datos = {
      sentence: text,
      // "Computers are exposed to a Device Lost Threat with prob 5.0 and impact 1.0",
      sentence_type: "process_asset_threat_impact_prob_sentence",
    };
  }
  if (type === "asset_threats_sentences") {
    datos = {
      sentence: text,
      // "intentional malware distribution and fire threatens Processes with impact 2 and probability 3",
      sentence_type: "process_asset_threats_sentence",
    };
  }
  if (type === "threat_risk_sentence") {
    datos = {
      sentence: text,
      // "privilege escalation may generate an accident risk, fire risk, flooding risk, natural disaster risk and terrorism attack",
      sentence_type: "process_threats_risks_sentence",
    };
  }

  // // Ejemplo implementando el metodo POST:

  // Opciones por defecto estan marcadas con un *
  const response = await fetch("http://127.0.0.1:5000/input", {
    headers: { "Content-Type": "application/json" },
    mode: "no-cors",
    method: "POST", // *GET, POST, PUT, DELETE, etc.
    body: JSON.stringify(datos), // body data type must match "Content-Type" header
  });
  // parses JSON response into native JavaScript objects
  console.log(datos);

  // getTerms();
};

const getTerms = async () => {
  let outputTerms;
  let peticion = await fetch("http://127.0.0.1:5000/output", {
    method: "GET",
    headers: { "Content-Type": "application/json" },
    mode: "cors",
  })
    .then((response) => {
      return response.json();
    })
    .then((recurso) => {
      outputTerms = recurso;
      console.log(recurso);
    });

  console.log(outputTerms["result"][0]);
};

const VoiceRecogniser = (props) => {
  let { transcript, resetTranscript } = useSpeechRecognition();

  console.log(document.getElementsByClassName("result").value);

  if (!SpeechRecognition.browserSupportsSpeechRecognition()) {
    return null;
  }

  // if (state["edit"]) transcript = state["transcript"];

  return (
    <div>
      <button
        onClick={SpeechRecognition.startListening}
        className="startButton"
      >
        Start
      </button>
      <button onClick={SpeechRecognition.stopListening} className="stopButton">
        Stop
      </button>
      <button onClick={resetTranscript} className="resetButton">
        Reset
      </button>
      {/* <p>{transcript}</p>  */}
      <div className="resultBox">
        <textarea
          type="textarea"
          defaultValue={transcript}
          className="result"
          rows="5"
          onChange={handleChange}
        ></textarea>
        <div>
          <div className="result">
            Choose the sentence type
            <select className="select">
              <option>anomaly_threat_sentences</option>
              <option>asset_threat_impact_prob</option>
              <option>asset_threats_sentences</option>
              <option>threat_risk_sentence</option>
            </select>
          </div>
          <button className="button" onClick={sendSentence}>
            Process Sentence
          </button>
          <button className="button">Build Rule</button>
          <button className="button" onClick={getTerms}>
            Get terms
          </button>
          <div>
            {/* {Object.entries(terms).map((i) => {
              return <Term termType={i[0]} term={i[1]} />;
            })} */}
          </div>
        </div>
        {/* <SentenceInput transcript={transcript} /> */}
        {/* <input type="text" defaultValue={transcript} className="result"></input> */}
      </div>
    </div>
  );
};

export default VoiceRecogniser;
