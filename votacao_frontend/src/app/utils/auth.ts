import Cookies from 'js-cookie';

const TOKEN_KEY = 'token';

export const login = (cpf: string) => {
  Cookies.set(TOKEN_KEY, cpf);
};

export const logout = () => {
  Cookies.remove(TOKEN_KEY);
};
