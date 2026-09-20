import { BrowserRouter, Route, Routes } from "react-router";
import { HomePage } from "./pages/HomePage";

export const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage/>}/>
      </Routes>
    </BrowserRouter>
  );
};
