import './App.css';
import { AssociateProvider } from './context/AssociateContext.js';
import AppRoutes from './Routes.js';

function App() {
  return (
    <AssociateProvider>
      <AppRoutes />
    </AssociateProvider>
  )
}

export default App;