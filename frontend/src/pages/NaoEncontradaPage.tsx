import { Link } from "react-router"
import { Layout } from "../components/ui/Layout"

export const NaoEncontradaPage = () => {

  return (
    <Layout>
      <h1>Página não encontrada</h1>
      <Link to="/">Voltar para as pautas</Link>
    </Layout>
  )
}
