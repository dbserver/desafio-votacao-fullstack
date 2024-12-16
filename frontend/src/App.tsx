"use client";
import MainContainer from "./ui/Util/main-container.tsx";
import {
    BrowserRouter as Router,
    Route, Routes,
} from "react-router-dom";

import Associated from "./pages/Associated";
import Agenda from "./pages/Agenda";
import Session from "./pages/Session";
import Home from "./Home";
import Vote from "./pages/Vote";
import VoteResult from "./pages/Vote/result.tsx";


export function App() {

  return (
        <MainContainer>
            <Router>
                <Routes>
                    <Route path="/" element={<Home />}></Route>
                    <Route path="/agenda" element={<Agenda />}></Route>
                    <Route path="/associated" element={<Associated />}></Route>
                    <Route path="/session" element={<Session />}></Route>
                    <Route path="/vote" element={<Vote />}></Route>
                    <Route path="/vote/result" element={<VoteResult />}></Route>
                </Routes>
            </Router>
        </MainContainer>
  )
}

export default App
