import AgendaForm from './components/AgendaForm';
import AgendaCard from './components/AgendaCard';
import { useAgenda } from './contexts/AgendaContext';

function App() {
  const { agendas, alert, status, refreshAgendas } = useAgenda();

  return (
    <div className="container py-4">
      <header className="pb-3 mb-4 border-bottom d-flex justify-content-between align-items-center">
        <h1 className="fs-3 text-dark mb-0">🗳️ Assembleia Digital - Cooperativa</h1>
        {
          status !== 'LOADING' && (
            <button
              type="button"
              className="btn btn-outline-primary btn-sm"
              onClick={() => void refreshAgendas()}
            >
              Atualizar
            </button>
          )
        }
      </header>

      {status === 'LOADING' && (
        <div className="alert alert-light" role="status">
          Carregando pautas...
        </div>
      )}

      {status !== 'LOADING' && (
        <>
          {alert.message && (
            <div className={`alert alert-${alert.type} alert-dismissible fade show`} role="alert">
              {alert.message}
            </div>
          )}

          <div className="row">
            {/* Formulário */}
            <div className="col-lg-4 mb-4">
              <AgendaForm />
            </div>

            {/* Cards de Pauta */}
            <div className="col-lg-8">
              <h4 className="mb-3">Pautas Cadastradas</h4>
              {status === 'ERROR' ? (
                <div className="alert alert-danger" role="alert">
                  <div>Não foi possível carregar as pautas.</div>
                  <button
                    type="button"
                    className="btn btn-sm btn-outline-danger mt-3"
                    onClick={() => refreshAgendas()}
                  >
                    Tentar novamente
                  </button>
                </div>
              ) : agendas.length === 0 ? (
                <p className="text-muted">Nenhuma pauta cadastrada até o momento.</p>
              ) : (
                <div>
                  {agendas.map((agenda) => (
                    <AgendaCard
                      key={agenda.id}
                      agenda={agenda}
                    />
                  ))}
                </div>
              )}
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default App;