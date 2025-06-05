import RestConsumer from './RestConsumer';


const SessionService = {
    createSession: (durationTime) =>
        RestConsumer.post(
            `/session?durationTime=${durationTime}`,
            {
                'Content-Type': 'text/plain'
            }
        ),

    getSessions: () => RestConsumer.get('/session', {
        'Content-Type': 'application/json',
    }),

    startSession: (sessionId) =>
        RestConsumer.post(`/startSession?sessionId=${sessionId}`),

    getAllSessionsNotInitialized: () => RestConsumer.get('/sessionNotInitialized', {
        'Content-Type': 'application/json',
    }),
};

export default SessionService;