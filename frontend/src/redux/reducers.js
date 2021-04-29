import { combineReducers } from "redux";
import transcriptReducer from "./transcriptReducer";

const GlobalState = combineReducers({
  transcript: transcriptReducer,
});

export default GlobalState;
