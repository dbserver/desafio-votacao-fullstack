import { Card } from "../components/ui/Card";
import { Layout } from "../components/ui/Layout";
import { PautaForm } from "../features/pauta/components/PautaForm";
import { PautaList } from "../features/pauta/components/PautaList";
import "./HomePage.css";

export const HomePage = () => {
  return (
    <Layout>
      <h1>Pautas</h1>

      <div className="home-page">
        <Card title="Nova pauta">
          <PautaForm />
        </Card>

        <Card title="Cadastradas">
          <PautaList />
        </Card>
      </div>
    </Layout>
  );
};
