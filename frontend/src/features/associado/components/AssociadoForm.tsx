import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { Button } from "../../../components/ui/Button";
import { Form, FormActions } from "../../../components/ui/Form";
import { TextField } from "../../../components/ui/TextField";
import { useCriarAssociado } from "../hooks";
import {
  criarAssociadoRequestSchema,
  type CriarAssociadoRequest,
} from "../schema";

export const AssociadoForm = () => {
  const criarAssociado = useCriarAssociado();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CriarAssociadoRequest>({
    resolver: zodResolver(criarAssociadoRequestSchema),
    defaultValues: { nome: "", cpf: "" },
  });

  const onSubmit = handleSubmit(async (request) => {
    try {
      const associado = await criarAssociado.mutateAsync(request);

      toast.success(`Associado ${associado.nome} cadastrado`);
      reset();
    } catch (erro) {
      toast.error(
        erro instanceof Error
          ? erro.message
          : "Não foi possível cadastrar o associado",
      );
    }
  });

  return (
    <Form onSubmit={onSubmit}>
      <TextField
        label="Nome"
        placeholder="Maria da Silva"
        error={errors.nome?.message}
        {...register("nome")}
      />

      <TextField
        label="CPF"
        placeholder="529.982.247-25"
        inputMode="numeric"
        error={errors.cpf?.message}
        {...register("cpf")}
      />

      <FormActions>
        <Button type="submit" loading={criarAssociado.isPending}>
          Cadastrar
        </Button>
      </FormActions>
    </Form>
  );
};
