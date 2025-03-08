<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h1>Dashboard</h1>
      <button @click="deslogar" class="btn-deslogar">Deslogar</button>
    </div>
    <nav class="tab-nav">
      <router-link to="/dashboard/pautas" class="tab-link">Pautas</router-link>
      <router-link to="/dashboard/sessoes" class="tab-link">Sessões</router-link>
    </nav>
    <div class="tab-content">
      <router-view />
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'DashboardView',
  data() {
    return {
      users: null, // Defina a propriedade users no data
    }
  },
  async created() {
    // Carregue os dados do usuário ao criar o componente
    const userId = localStorage.getItem('userId')
    if (userId) {
      try {
        const response = await axios.get(`http://localhost:8000/api/users/${userId}`)
        this.users = response.data
      } catch (error) {
        console.error('Erro ao carregar dados do usuário:', error)
      }
    }
  },
  methods: {
    deslogar() {
      // Limpa o localStorage
      localStorage.removeItem('userId')

      // Redireciona para a tela de login
      this.$router.push('/')
    },
  },
}
</script>

<style scoped>
.dashboard-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background: linear-gradient(135deg, #2c3e50, #34495e); /* Gradiente azul escuro */
  border-radius: 15px;
  color: #ffffff; /* Texto branco para contraste */
}

h1 {
  font-size: 40px;
  /* margin-bottom: 20px; */
  text-align: center;
}

.tab-nav {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  border-bottom: 2px solid rgba(255, 255, 255, 0.1); /* Borda sutil */
}

.tab-link {
  padding: 10px 20px;
  margin: 0 10px;
  font-size: 18px;
  color: rgba(255, 255, 255, 0.7); /* Texto branco semi-transparente */
  text-decoration: none;
  border-radius: 8px 8px 0 0;
  transition: all 0.3s ease;
}

.tab-link:hover {
  background: rgba(255, 255, 255, 0.1); /* Fundo semi-transparente ao passar o mouse */
  color: #ffffff; /* Texto branco */
}

.tab-link.router-link-exact-active {
  color: #ffffff; /* Texto branco */
  border-bottom: 3px solid #3498db; /* Azul claro para aba ativa */
  font-weight: bold;
}

.tab-content {
  padding: 20px;
  background: rgba(255, 255, 255, 0.1); /* Fundo semi-transparente */
  border-radius: 8px;
}

.dashboard-header {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  position: relative;
}

.btn-deslogar {
  padding: 10px 20px;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  transition: background-color 0.3s ease;
  position: absolute;
  top: 0;
  right: 0;
}

.btn-deslogar:hover {
  background-color: #c0392b;
}
</style>
