import { format } from "date-fns";
import { ptBR } from "date-fns/locale";

export const somenteDigitos = (cpf: string) => cpf.replace(/\D/g, "");

export const formatarCpf = (cpf: string) => {
  const digitos = somenteDigitos(cpf);

  if (digitos.length !== 11) {
    return cpf;
  }

  return digitos.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
};

export const formatarDataHora = (iso: string) => {
  return format(new Date(iso), "dd/MM/yyyy HH:mm", { locale: ptBR });
};
