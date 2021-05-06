import React, { Component } from "react";
import "./Term.css";

class Term extends Component {
  constructor(props) {
    super(props);
    this.state = {
      termType: this.props.termType,
      Term: this.props.Term,
    };
  }

  static getDerivedStateFromProps = (props, state) => {
    return {
      termType: props.termType,
      Term: props.Term,
    };
  };

  render() {
    return (
      <div className="result">
        {this.props.termType}
        {console.log(this.props.term)}
        <select className="custom-select">
          <option>{this.props.term}</option>
        </select>
      </div>
    );
  }
}

export default Term;
