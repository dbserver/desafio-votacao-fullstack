import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 100,
    duration: '30s',
};

export default function () {
    const cpfFake = Math.floor(10000000000 + Math.random() * 90000000000).toString();
    
    const payload = JSON.stringify({
        sessaoVotacaoId: 'ad0c8721-6fb3-4e5a-a236-7285f4e2801d',
        cpf: cpfFake,
        vote: Math.random() > 0.5 ? 'SIM' : 'NAO'
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const url = 'http://localhost:8080/api/v1/votos';

    const res = http.post(url, payload, params);

    check(res, {
        'status 201 (Criado com sucesso)': (r) => r.status === 201,
        'status 404 (Barrado pelo FakeClient)': (r) => r.status === 404,
        'status 400 ou 422 (Erro de regra/validacao)': (r) => r.status === 400 || r.status === 422,
    });

    // Pequeno respiro para simular o tempo de rede real
    sleep(0.1);
}