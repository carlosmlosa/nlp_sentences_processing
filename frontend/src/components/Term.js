import React from "react";
import "./Term.css";
const Term = (props) => {
  return (
    <div className="result">
      {props.termType}
      {console.log(props.term)}
      <select className="custom-select">
        <option>{props.term}</option>
      </select>
    </div>
  );
};

export default Term;
