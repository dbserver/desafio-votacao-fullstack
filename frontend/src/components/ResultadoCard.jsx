export default function ResultadoCard({ resultado }) {
  if (!resultado) return null;

  return (
    <div>
      <h3>Resultado da Sessão</h3>
      <p>Votos SIM: {resultado.votosSim}</p>
      <p>Votos NÃO: {resultado.votosNao}</p>
    </div>
  );
}
