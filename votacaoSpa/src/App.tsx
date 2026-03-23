import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import ResponsiveLayout from './components/layout/ResponsiveLayout'
import { ToastProvider } from './components/ui/ToastProvider'
import PautasPage from './pages/PautasPage'
import VotarPage from './pages/VotarPage'

function App() {
  return (
    <ToastProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<ResponsiveLayout />}>
            <Route index element={<Navigate to="pautas" replace />} />
            <Route path="pautas" element={<PautasPage />} />
            <Route path="votar/:pautaId/:sessaoId" element={<VotarPage />} />
            <Route path="*" element={<Navigate to="pautas" replace />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </ToastProvider>
  )
}

export default App
