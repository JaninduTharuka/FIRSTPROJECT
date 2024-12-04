// App.js
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage from "./screens/HomePage";
import AddStudent from "./screens/AddStudent";
import ViewStudent from "./screens/ViewStudent";

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/add-student" element={<AddStudent />} />
        <Route path="/view-student" element={<ViewStudent />} />
      </Routes>
    </Router>
  );
};

export default App;
