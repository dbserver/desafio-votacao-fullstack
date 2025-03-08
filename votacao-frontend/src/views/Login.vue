<template>
  <div class="login-container">
    <h1>Login</h1>
    <div v-if="usuarios.length > 0">
      <h2>Selecione um associado:</h2>
      <ul class="user-list">
        <li v-for="usuario in usuarios" :key="usuario.id">
          <a href="#" @click.prevent="selecionarUsuario(usuario)" class="user-link">
            {{ usuario.name }}
          </a>
        </li>
      </ul>
    </div>
    <p v-else class="no-users">Associados não cadastrados</p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'UserLogin',
  data() {
    return {
      usuarios: [],
    }
  },
  async created() {
    // Busca a lista de usuários do back-end
    try {
      const response = await axios.get('http://localhost:8000/api/users')
      this.usuarios = response.data
    } catch (error) {
      console.error('Erro ao buscar usuários:', error)
    }
  },
  methods: {
    selecionarUsuario(usuario) {
      // Simula o "login" selecionando o usuário
      localStorage.setItem('userId', usuario.id.toString()) // Salva o usuário no localStorage
      this.$router.push('/dashboard') // Redireciona para o dashboard
    },
  },
}
</script>

<style scoped>
.login-container {
  max-width: 30vw;
  margin: 50px auto;
  padding: 30px;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  text-align: center;
  color: #fff;
}

h1 {
  font-size: 28px;
  margin-bottom: 20px;
  font-weight: bold;
}

h2 {
  font-size: 20px;
  margin-bottom: 25px;
  font-weight: 500;
}

.user-list {
  list-style-type: none;
  padding: 0;
}

.user-list li {
  margin: 15px 0;
}

.user-link {
  display: block;
  padding: 15px;
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  text-decoration: none;
  border-radius: 10px;
  transition:
    background 0.3s ease,
    transform 0.2s ease;
  font-size: 18px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.user-link:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-5px);
}

.user-name {
  display: block;
}

.no-users {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
}
</style>
