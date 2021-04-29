import { EDIT_TRANSCRIPT, GET_TRANSCRIPT } from "./actions";
import { transcript } from "../constants/constants";

export default function transcriptReducer(
  state = { transcript: transcript },
  action
) {
  switch (action.type) {
    case "EDIT_TRANSCRIPT":
      console.log(action.newTranscript);
      return (transcript = action.newTranscript);
    case "GET_TRANSCRIPT":
      return state;
    default:
      return state;
  }
}
