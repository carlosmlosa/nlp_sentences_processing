// import "webkitSpeechRecognition";

// export default function startRecording() {
//   if (window.hasOwnProperty("webkitSpeechRecognition")) {
//     var recognition =
//       new webkitSpeechRecognition() ||
//       window.SpeechRecognition ||
//       window.webkitSpeechRecognition;
//     recognition.continuous = false;
//     recognition.interimResults = false;
//     recognition.lang = "en-US";
//     recognition.start();

//     recognition.onresult = function (e) {
//       document.getElementById("transcript").value = e.results[0][0].transcript;
//       console.log(e.results[0][0].transcript);
//       recognition.stop();
//       //document.getElementById("speak-form").submit();
//     };
//     recognition.onerror = function (e) {
//       recognition.stop();
//     };
//   }
// }

import React from "react";
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
      <button onClick={SpeechRecognition.startListening}>Start</button>
      <button onClick={SpeechRecognition.stopListening}>Stop</button>
      <button onClick={resetTranscript}>Reset</button>
      <p>{transcript}</p>
    </div>
  );
};
export default VoiceRecogniser;
