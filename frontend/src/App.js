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
          <p>Click start button to record the rule or write and process it</p>
          <p>Click stop button whenever you finish</p>

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
