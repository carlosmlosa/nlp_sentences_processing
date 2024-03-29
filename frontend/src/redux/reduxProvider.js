import { transcript } from "../constants/constants";
import { Provider } from "react-redux";
import GlobalState from "./reducers";
import { createStore } from "redux";
import React from "react";
import App from "../App";

export default class ReduxProvider extends React.Component {
  constructor(props) {
    super(props);
    this.initialState = { transcript: transcript };
    this.store = createStore(GlobalState, this.initialState);
  }

  render() {
    return (
      <Provider store={this.store}>
        <div style={{ height: "100%" }}>
          <App store={this.store} />
        </div>
      </Provider>
    );
  }
}
