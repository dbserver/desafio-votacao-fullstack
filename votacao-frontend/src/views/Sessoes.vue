<template>
  <div class="sessoes-container">
    <h2>Sessões</h2>
    <button @click="abrirModalCriarSessao" class="btn-criar-sessao">Abrir Nova Sessão</button>
    <ul v-if="sessoes.length > 0" class="lista-sessoes">
      <li v-for="sessao in sessoes" :key="sessao.id" class="sessao-item">
        <div class="sessao-info">
          <h3>{{ sessao.pauta.nome }}</h3>
          <!-- Nome da sessão é o título da pauta -->
          <p><strong>Descrição:</strong> {{ sessao.pauta.descricao }}</p>
          <p><strong>Status:</strong> {{ verificarStatus(sessao) }}</p>
          <p v-if="sessao.data_inicio">
            <strong>Data de Início:</strong> {{ formatarData(sessao.data_inicio) }}
          </p>
          <p v-if="sessao.data_final">
            <strong>Data Final:</strong> {{ formatarData(sessao.data_final) }}
          </p>
        </div>
        <div class="inner-button">
          <button
            @click="abrirModalVotar(sessao)"
            class="btn-votar"
            :disabled="verificarStatus(sessao) !== 'Aberta' || jaVotou(sessao)"
            :class="{
              'btn-votar-disabled': verificarStatus(sessao) !== 'Aberta' || jaVotou(sessao),
            }"
          >
            Votar
          </button>
          <button
            @click="mostrarResultado(sessao)"
            :disabled="verificarStatus(sessao) !== 'Fechada'"
            :class="{
              'btn-resultado-disabled': verificarStatus(sessao) !== 'Fechada',
            }"
          >
            Resultado da votação
          </button>
        </div>
        <div v-if="sessao.mostrarResultado" class="resultado">
          <div class="barra-sim" :style="{ '--percentual-sim': sessao.percentualSim + '%' }">
            SIM: {{ sessao.percentualSim }}%
          </div>
          <div class="barra-nao" :style="{ '--percentual-nao': sessao.percentualNao + '%' }">
            NÃO: {{ sessao.percentualNao }}%
          </div>
        </div>
      </li>
    </ul>
    <p v-else class="sem-sessoes">Nenhuma sessão cadastrada.</p>

    <!-- Modal para criar nova sessão -->
    <div v-if="modalAberto" class="modal">
      <div class="modal-conteudo">
        <h3>Abrir Nova Sessão</h3>
        <form @submit.prevent="criarSessao">
          <div class="form-group">
            <label for="pauta_id">Pauta:</label>
            <select v-model="novaSessao.pauta_id" id="pauta_id" required>
              <option v-for="pauta in pautas" :key="pauta.id" :value="pauta.id">
                {{ pauta.nome }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="data_inicio">Data de Início (opcional):</label>
            <input v-model="novaSessao.data_inicio" id="data_inicio" type="datetime-local" />
          </div>
          <div class="form-group">
            <label for="data_final">Data Final (opcional):</label>
            <input v-model="novaSessao.data_final" id="data_final" type="datetime-local" />
          </div>
          <div class="form-botoes">
            <button type="button" @click="fecharModal" class="btn-cancelar">Cancelar</button>
            <button type="submit" class="btn-salvar">Salvar</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal para votar -->
    <div v-if="modalVotarAberto" class="modal">
      <div class="modal-conteudo">
        <h3>{{ sessaoSelecionada.pauta.nome }}</h3>
        <p>{{ sessaoSelecionada.pauta.descricao }}</p>
        <div class="form-botoes">
          <button @click="votar('sim')" class="btn-votar-sim">SIM</button>
          <button @click="votar('nao')" class="btn-votar-nao">NÃO</button>
          <button @click="fecharModalVotar" class="btn-cancelar">Fechar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'SessaoList',
  data() {
    return {
      sessoes: [],
      pautas: [],
      modalAberto: false,
      modalVotarAberto: false,
      novaSessao: {
        pauta_id: '',
        data_inicio: null,
        data_final: null,
      },
      sessaoSelecionada: {},
      votos: [],
    }
  },
  async created() {
    await this.carregarDados()
  },
  methods: {
    async carregarDados() {
      try {
        await this.carregarPautas() // Carrega as pautas primeiro
        await this.carregarSessoes() // Depois carrega as sessões
        await this.carregarVotos() // Por fim, carrega os votos
      } catch (error) {
        console.error('Erro ao carregar dados:', error)
      }
    },
    async carregarPautas() {
      try {
        const response = await axios.get('http://localhost:8000/api/pautas')
        this.pautas = response.data
      } catch (error) {
        console.error('Erro ao carregar pautas:', error)
      }
    },
    async carregarSessoes() {
      try {
        const response = await axios.get('http://localhost:8000/api/sessoes')
        this.sessoes = response.data.map((sessao) => {
          const pauta = this.pautas.find((p) => p.id === sessao.pauta_id)
          return { ...sessao, pauta, mostrarResultado: false, percentualSim: 0, percentualNao: 0 }
        })
      } catch (error) {
        console.error('Erro ao carregar sessões:', error)
      }
    },
    async carregarVotos() {
      try {
        const response = await axios.get('http://localhost:8000/api/votos')
        this.votos = response.data
        this.sessoes.forEach((sessao) => {
          this.calcularResultado(sessao)
        })
      } catch (error) {
        console.error('Erro ao carregar votos:', error)
      }
    },
    async login() {
      try {
        const response = await axios.post('http://localhost:8000/api/users', {
          nome: this.nome,
          email: this.email,
        })

        // Verifica se o user_id foi retornado pelo backend
        if (response.data.user && response.data.user.id) {
          // Armazena o user_id no localStorage
          localStorage.setItem('userId', response.data.user.id.toString())
        } else {
          console.error('ID do usuário não encontrado na resposta do backend.')
          alert('Erro ao fazer login. ID do usuário não encontrado.')
          return
        }

        // Redireciona para a página de sessões
        this.$router.push('/sessoes')
      } catch (error) {
        console.error('Erro ao fazer login:', error)
        alert('Erro ao fazer login. Verifique o console para mais detalhes.')
      }
    },
    verificarStatus(sessao) {
      const agora = new Date()
      const dataInicio = sessao.data_inicio ? new Date(sessao.data_inicio) : null
      const dataFinal = sessao.data_final ? new Date(sessao.data_final) : null

      if (dataInicio && agora < dataInicio) {
        return 'Aguardando início'
      } else if (dataFinal && agora > dataFinal) {
        return 'Fechada'
      } else {
        return 'Aberta'
      }
    },
    jaVotou(sessao) {
      const userId = localStorage.getItem('userId')

      if (!userId) {
        console.error('ID do usuário não encontrado.')
        alert('Usuário não autenticado. Faça login para votar.')
        return false // Retorna false se o user_id não estiver disponível
      }

      const userIdInt = parseInt(userId, 10)

      if (isNaN(userIdInt)) {
        console.error('ID do usuário não é um número válido.')
        alert('Erro ao processar o ID do usuário.')
        return false
      }

      return this.votos.some((voto) => voto.sessao_id === sessao.id && voto.user_id === userIdInt)
    },
    abrirModalCriarSessao() {
      this.modalAberto = true
    },
    fecharModal() {
      this.modalAberto = false
      this.novaSessao = { pauta_id: '', data_inicio: null, data_final: null }
    },
    abrirModalVotar(sessao) {
      this.sessaoSelecionada = sessao
      this.modalVotarAberto = true
    },
    fecharModalVotar() {
      this.modalVotarAberto = false
    },
    mostrarResultado(sessao) {
      this.calcularResultado(sessao)
      sessao.mostrarResultado = true
    },
    async criarSessao() {
      try {
        const dadosParaEnviar = {
          pauta_id: this.novaSessao.pauta_id,
        }

        if (this.novaSessao.data_inicio) {
          dadosParaEnviar.data_inicio = new Date(this.novaSessao.data_inicio)
            .toISOString()
            .slice(0, 19)
            .replace('T', ' ')
        }

        if (this.novaSessao.data_final) {
          dadosParaEnviar.data_final = new Date(this.novaSessao.data_final)
            .toISOString()
            .slice(0, 19)
            .replace('T', ' ')
        }

        const response = await axios.post('http://localhost:8000/api/sessoes', dadosParaEnviar)
        const pauta = this.pautas.find((p) => p.id === response.data.pauta_id)
        this.sessoes.push({
          ...response.data,
          pauta,
          mostrarResultado: false,
          percentualSim: 0,
          percentualNao: 0,
        })

        this.fecharModal()
        alert('Sessão criada com sucesso!')
      } catch (error) {
        console.error('Erro ao criar sessão:', error)
        alert('Erro ao criar sessão. Verifique o console para mais detalhes.')
      }
    },
    async votar(opcao) {
      try {
        const votoBoolean = opcao === 'sim' ? true : false
        const userId = localStorage.getItem('userId') // Obtenha o user_id do localStorage

        if (!userId) {
          alert('Usuário não autenticado. Faça login para votar.')
          return
        }

        const userIdInt = parseInt(userId, 10)

        if (isNaN(userIdInt)) {
          console.error('ID do usuário não é um número válido.')
          alert('Erro ao processar o ID do usuário.')
          return
        }

        const dadosParaEnviar = {
          sessao_id: this.sessaoSelecionada.id,
          user_id: userIdInt, // Adicionar user_id convertido
          voto: votoBoolean, // Enviar true ou false
        }

        const response = await axios.post('http://localhost:8000/api/votos', dadosParaEnviar)

        this.votos.push(response.data)
        this.calcularResultado(this.sessaoSelecionada)
        this.fecharModalVotar()
        alert('Voto registrado com sucesso!')
      } catch (error) {
        console.error('Erro ao registrar voto:', error)
        console.error('Detalhes do erro:', error.response?.data)
        alert('Erro ao registrar voto. Verifique o console para mais detalhes.')
      }
    },
    calcularResultado(sessao) {
      const votosSessao = this.votos.filter((voto) => voto.sessao_id === sessao.id)
      const totalVotos = votosSessao.length
      const votosSim = votosSessao.filter((voto) => voto.voto === true).length
      const votosNao = votosSessao.filter((voto) => voto.voto === false).length

      sessao.percentualSim = totalVotos ? (votosSim / totalVotos) * 100 : 0
      sessao.percentualNao = totalVotos ? (votosNao / totalVotos) * 100 : 0
    },
    formatarData(data) {
      return new Date(data).toLocaleString()
    },
  },
}
</script>

<style scoped>
.sessoes-container {
  padding: 20px;
}

.sessoes-container h2 {
  font-size: 24px;
  margin-bottom: 10px;
}

.btn-criar-sessao {
  padding: 10px 20px;
  background: #3498db;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s ease;
  margin-bottom: 20px;
}

.btn-criar-sessao:hover {
  background: #2980b9;
}

.lista-sessoes {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  list-style-type: none;
  padding: 0;
}

.sessao-item {
  background: linear-gradient(135deg, #ffffff, #f9f9f9);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.sessao-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.sessao-info {
  margin-bottom: 10px;
}

.sessao-info h3 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #2c3e50;
}

.sessao-info p {
  font-size: 14px;
  color: #666;
  margin: 5px 0;
}

.status-badge {
  display: inline-block;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  text-transform: uppercase;
}

.status-aberta {
  background-color: #4caf50;
  color: white;
}

.status-fechada {
  background-color: #e74c3c;
  color: white;
}

.inner-button {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.btn-votar,
.btn-resultado {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-votar {
  padding: 5px 10px;
  background: #2ecc71;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-votar:hover {
  background: #27ae60;
}

.btn-votar-disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background-color: #ccc; /* Cor de fundo para botão desabilitado */
}

.btn-resultado-disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.barra-sim,
.barra-nao {
  width: 100%; /* Ambas as barras têm 100% de largura */
  height: 30px;
  line-height: 30px;
  border-radius: 8px;
  margin-bottom: 10px;
  padding-left: 10px;
  font-size: 12px;
  font-weight: bold;
  color: white;
  text-align: left; /* Alinha o texto à esquerda */
  position: relative; /* Permite posicionar o pseudo-elemento */
  overflow: hidden; /* Esconde o conteúdo que ultrapassar a barra */
  z-index: 1;
}

.barra-sim {
  color: black; /* Texto branco para "SIM" */
  border: 2px solid #2ecc71; /* Borda verde para "SIM" */
  background-color: transparent; /* Fundo transparente */
}

.barra-nao {
  color: black; /* Texto vermelho para "NÃO" */
  border: 2px solid #e74c3c; /* Borda vermelha para "NÃO" */
  background-color: transparent; /* Fundo transparente */
}

.barra-sim::before,
.barra-nao::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  /* border-radius: 8px; */
  z-index: -1; /* Coloca o preenchimento atrás do texto */
}

.barra-sim::before {
  background-color: #2ecc71; /* Preenchimento verde para "SIM" */
  width: var(--percentual-sim, 100%); /* Largura dinâmica baseada no percentual */
}

.barra-nao::before {
  background-color: #e74c3c; /* Preenchimento vermelho para "NÃO" */
  width: var(--percentual-nao, 100%); /* Largura dinâmica baseada no percentual */
}

.sem-sessoes {
  color: rgba(255, 255, 255, 0.8);
}

.resultado {
  margin-top: 20px;
}

/* Estilos do modal */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1;
}

.modal-conteudo {
  background: #2c3e50;
  padding: 20px;
  border-radius: 10px;
  width: 400px;
}

.modal-conteudo h3 {
  margin-top: 0;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: rgba(255, 255, 255, 0.8);
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.form-group option {
  background: #2c3e50;
}

.form-botoes {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn-cancelar {
  padding: 10px 20px;
  background: #e74c3c;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-cancelar:hover {
  background: #c0392b;
}

.btn-salvar {
  padding: 10px 20px;
  background: #3498db;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-salvar:hover {
  background: #2980b9;
}

.btn-votar-sim {
  padding: 10px 20px;
  background: #2ecc71;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-votar-sim:hover {
  background: #27ae60;
}

.btn-votar-nao {
  padding: 10px 20px;
  background: #e74c3c;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-votar-nao:hover {
  background: #c0392b;
}
</style>
