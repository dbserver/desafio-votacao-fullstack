import React from 'react'
import ReactDOM from 'react-dom/client'
import BasicCard from './Components/Card'
import Home from './pages/Home'
import App from './App'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
    <BasicCard />
  </React.StrictMode>,
)
