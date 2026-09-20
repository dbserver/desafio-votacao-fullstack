import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { Button } from "../../../components/ui/Button";
import { Form, FormActions } from "../../../components/ui/Form";
import { TextField } from "../../../components/ui/TextField";
import { onlyNumbers, withMask } from "../../../lib/mask";
import { useAbrirSessao } from "../hooks";
import { criarSessaoRequestSchema, type CriarSessaoRequest } from "../schema";

type SessaoFormProps = {
  pautaId: number;
};

export const SessaoForm = ({ pautaId }: SessaoFormProps) => {
  const abrirSessao = useAbrirSessao(pautaId);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CriarSessaoRequest>({
    resolver: zodResolver(criarSessaoRequestSchema),
  });

  const { onChange, ...duracao } = register("duracaoEmMinutos", {
    setValueAs: (valor) => (valor === "" ? undefined : Number(valor)),
  });

  const onSubmit = handleSubmit(async (request) => {
    try {
      await abrirSessao.mutateAsync(request);

      toast.success("Sessão de votação aberta");
    } catch (erro) {
      toast.error(
        erro instanceof Error ? erro.message : "Não foi possível abrir a sessão",
      );
    }
  });

  return (
    <Form onSubmit={onSubmit}>
      <TextField
        label="Duração em minutos (opcional)"
        placeholder="1"
        type="text"
        inputMode="numeric"
        maxLength={4}
        error={errors.duracaoEmMinutos?.message}
        onChange={withMask(onlyNumbers, onChange)}
        {...duracao}
      />

      <FormActions>
        <Button type="submit" loading={abrirSessao.isPending}>
          Abrir sessão
        </Button>
      </FormActions>
    </Form>
  );
};
