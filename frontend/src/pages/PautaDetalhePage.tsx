import { useParams } from "react-router"
import { Layout } from "../components/ui/Layout"

export const PautaDetalhePage = () => {
  const { pautaId } = useParams()

  return (
    <Layout>
      <h1>Pauta {pautaId}</h1>
    </Layout>
  )
}
