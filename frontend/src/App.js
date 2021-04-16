import logo from "./logo.svg";
import "./App.css";
import VoiceRecogniser from "./components/VoiceRecogniser.js";
function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>Carlos Muñoz Losa</p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        ></a>
        <VoiceRecogniser />
      </header>
    </div>
  );
}

export default App;
