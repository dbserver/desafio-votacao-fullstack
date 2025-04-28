import './App.css';
import Routes from './routes/index';
import { Layout } from 'antd';

function App() {
  return (
    <Layout style={{ minHeight: '100vh' }}>
        <div className="background">
        <Routes />
        </div>
    </Layout>
  );
}

export default App;
