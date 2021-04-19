import "./VoiceRecogniser.css";
import React from "react";
import Term from "./Term.js";
import SpeechRecognition, {
  useSpeechRecognition,
} from "react-speech-recognition";

const VoiceRecogniser = () => {
  const { transcript, resetTranscript } = useSpeechRecognition();

  if (!SpeechRecognition.browserSupportsSpeechRecognition()) {
    return null;
  }

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
        <input className="result" value={transcript} rows="3"></input>
        {/* <textarea name="Text1" cols="40" rows="5" value={transcript}></textarea> */}
        <Term />
      </div>
    </div>
  );
};
export default VoiceRecogniser;
