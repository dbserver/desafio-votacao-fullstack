import { Card } from "../components/ui/Card";
import { Layout } from "../components/ui/Layout";
import { AssociadoForm } from "../features/associado/components/AssociadoForm";
import { AssociadoList } from "../features/associado/components/AssociadoList";
import "./AssociadosPage.css";

export const AssociadosPage = () => {
  return (
    <Layout>
      <h1>Associados</h1>

      <div className="associados-page">
        <Card title="Novo associado">
          <AssociadoForm />
        </Card>

        <Card title="Cadastrados">
          <AssociadoList />
        </Card>
      </div>
    </Layout>
  );
};
