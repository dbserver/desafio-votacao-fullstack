export function requireAuth(to, from, next) {
  const usuario = JSON.parse(localStorage.getItem('usuario'))
  if (usuario) {
    next()
  } else {
    next('/')
  }
}
