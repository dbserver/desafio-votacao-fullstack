import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import HomePage from "./pages/HomePage";
import PautaDetailPage from "./pages/PautaDetailPage";

function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/pautas/:id" element={<PautaDetailPage />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}

export default App;
