interface AgendaResultProps {
    yesVotes: number;
    noVotes: number;
    totalVotes: number;
}

function AgendaResult({ yesVotes, noVotes, totalVotes }: AgendaResultProps) {
    return (
        <div className="bg-dark text-white p-3 rounded">
            <h6 className="fw-bold border-bottom pb-2 mb-2">Resultado da Votação</h6>
            <div className="d-flex justify-content-between mb-1">
                <span>Votos "Sim":</span>
                <span className="badge bg-success fs-6">{yesVotes}</span>
            </div>
            <div className="d-flex justify-content-between mb-1">
                <span>Votos "Não":</span>
                <span className="badge bg-danger fs-6">{noVotes}</span>
            </div>
            <div className="d-flex justify-content-between border-top pt-2 mt-2">
                <strong>Total de Votos:</strong>
                <strong>{totalVotes}</strong>
            </div>
        </div>
    );
}

export default AgendaResult;