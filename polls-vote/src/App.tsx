import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { BrowserRouter } from 'react-router-dom'
import { AppRoutes } from './routes/app.routes'

function App() {
  const [count, setCount] = useState(0)

  return (
    <BrowserRouter>
      <AppRoutes />
    </BrowserRouter>
    // <>
    //   <div>
    //   </div>
    //   <h1>Sistema Votação</h1>
    //   <div className="container-enter-button card">
    //     <button onClick={() => setCount((count) => count + 1)}>
    //       Criar Pauta
    //     </button>
    //     <button onClick={() => setCount((count) => count + 1)}>
    //       Votar
    //     </button>
    //     {/*<p>
    //       Edit <code>src/App.tsx</code> and save to test HMR
    //     </p>*/}
    //   </div>
    //   {/*<p className="read-the-docs">
    //     Click on the Vite and React logos to learn more
    //   </p>*/}
    // </>
  )
}

export default App
