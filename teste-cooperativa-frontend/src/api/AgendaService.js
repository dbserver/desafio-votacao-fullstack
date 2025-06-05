import RestConsumer from './RestConsumer';


const AgendaService = {
  createAgenda: (description) =>
    RestConsumer.post(
      '/agenda',
      description,
      {
        'Content-Type': 'text/plain'
      }
    ),

  getAgendas: () => RestConsumer.get('/agenda', {
    'Content-Type': 'application/json',
  }),

  addAgendaToSession: (agendaId, sessionId) =>
    RestConsumer.post(`/addAgendaToSession?agendaId=${agendaId}&sessionId=${sessionId}`),

  getAgendasBySessionId: (sessionId) =>
    RestConsumer.get(`/agenda/session?sessionId=${sessionId}`, {
      'Content-Type': 'application/json',
    }),
};

export default AgendaService;