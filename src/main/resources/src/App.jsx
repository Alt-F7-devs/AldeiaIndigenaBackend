import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import HomeAluno from "./pages/HomeAluno";
import HomeProfessor from "./pages/HomeProfessor";
import BlibliotecaJogos from "./pages/BlibliotecaJogos";
import ConquistasAluno from "./pages/ConquistasAluno";
import GerenciaSala from "./pages/GerenciaSala";
import Historico from "./pages/HistoricoAtv";
import ListaAluno from "./pages/ListaAluno";
import SalaAluno from "./pages/SalaAluno";
import SalaProfessor from "./pages/SalaProfessor";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/" element={<HomeAluno />} />
        <Route path="/" element={<HomeProfessor />} />
        <Route path="/" element={<BlibliotecaJogos />} />
        <Route path="/" element={<ConquistasAluno />} />
        <Route path="/" element={<GerenciaSala />} />
        <Route path="/" element={<HistoricoAtv />} />
        <Route path="/" element={<listaAluno />} />
        <Route path="/" element={<SalaAluno />} />
        <Route path="/" element={<SalaProfessor />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;