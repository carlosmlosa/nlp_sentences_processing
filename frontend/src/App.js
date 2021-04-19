import logo from "./logo.svg";
import logo2 from "./logoProtege.jpg";
import microfono from "./micro.svg";
import soundwave from "./soundwave.svg";
import "./App.css";
import VoiceRecogniser from "./components/VoiceRecogniser.js";
function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={soundwave} className="App-logo" alt="logo" />
        <div className="Instructions">
          <p>Click start button to record the rule and process it</p>
          <p>Click stop button whenever you finish</p>
          <p>
            If you are not happy with the result you can either reset or edit
          </p>
          <VoiceRecogniser />
        </div>
      </header>
      <footer>
        <p>Carlos Muñoz Losa</p>
      </footer>
    </div>
  );
}

export default App;
