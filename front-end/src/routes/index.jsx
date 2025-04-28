// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AllDiscuss from '../pages/AllDiscuss';

const App = () => {
  return (
    <Router>
        <Routes>
          <Route path="/" element={<AllDiscuss />} />
        </Routes>
    </Router>
  );
};

export default App;
