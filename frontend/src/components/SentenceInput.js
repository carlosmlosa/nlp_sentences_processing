import React, { Component } from "react";
import Term from "./Term.js";
import "./SentenceInput.css";

class SentenceInput extends Component {
  constructor(props) {
    super(props);
    this.textChanged = this.textChanged.bind(this);
    this.propsChange = this.textChanged.bind(this);
    this.state = {
      transcript: this.props.transcript,
      edited: false,
    };
  }

  textChanged = (e) => {
    this.setState({
      transcript: e.target.value,
      edited: true,
    });
  };

  static getDerivedStateFromProps = (props, state) => {
    return {
      transcript: props.transcript,
    };
  };

  sendSentence = async () => {
    const type = document.getElementsByClassName("select")[0].value;
    let datos = {};
    if (type === "anomaly_threat_sentences") {
      datos = {
        sentence:
          "wifi sensor generates privilege escalation with probability 2 and impact 5",
        sentence_type: "process_anomaly_threat_sentence",
      };
    }
    if (type === "process_asset_threat_impact_prob_sentence") {
      datos = {
        sentence:
          "Computers are exposed to a Device Lost Threat with prob 5.0 and impact 1.0",
        sentence_type: "process_asset_threat_impact_prob_sentence",
      };
    }
    if (type === "asset_threats_sentences") {
      datos = {
        sentence:
          "intentional malware distribution and fire threatens Processes with impact 2 and probability 3",
        sentence_type: "process_asset_threats_sentence",
      };
    }
    if (type === "threat_risk_sentence") {
      datos = {
        sentence:
          "privilege escalation may generate an accident risk, fire risk, flooding risk, natural disaster risk and terrorism attack",
        sentence_type: "process_threats_risks_sentence",
      };
    }

    // // Ejemplo implementando el metodo POST:

    // Opciones por defecto estan marcadas con un *
    const response = await fetch("http://127.0.0.1:5000/input", {
      headers: { "Content-Type": "application/json" },
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify(datos), // body data type must match "Content-Type" header
    });
    // parses JSON response into native JavaScript objects
    console.log(datos);
  };

  render() {
    return (
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
        <button className="button" onClick={this.sendSentence}>
          Process Sentence
        </button>
        <button className="button">Build Rule</button>
      </div>
    );
  }
}

export default SentenceInput;
