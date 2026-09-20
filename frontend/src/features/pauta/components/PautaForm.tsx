import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { Button } from "../../../components/ui/Button";
import { Form, FormActions } from "../../../components/ui/Form";
import { TextArea } from "../../../components/ui/TextArea";
import { TextField } from "../../../components/ui/TextField";
import { useCriarPauta } from "../hooks";
import { criarPautaRequestSchema, type CriarPautaRequest } from "../schema";

export const PautaForm = () => {
  const criarPauta = useCriarPauta();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CriarPautaRequest>({
    resolver: zodResolver(criarPautaRequestSchema),
    defaultValues: { titulo: "", descricao: "" },
  });

  const onSubmit = handleSubmit(async (request) => {
    try {
      const pauta = await criarPauta.mutateAsync(request);

      toast.success(`Pauta "${pauta.titulo}" criada`);
      reset();
    } catch (erro) {
      toast.error(
        erro instanceof Error ? erro.message : "Não foi possível criar a pauta",
      );
    }
  });

  return (
    <Form onSubmit={onSubmit}>
      <TextField
        label="Título"
        placeholder="Reforma do estatuto"
        error={errors.titulo?.message}
        {...register("titulo")}
      />

      <TextArea
        label="Descrição"
        placeholder="Aprovar a nova redação do estatuto"
        rows={4}
        error={errors.descricao?.message}
        {...register("descricao")}
      />

      <FormActions>
        <Button type="submit" loading={criarPauta.isPending}>
          Criar pauta
        </Button>
      </FormActions>
    </Form>
  );
};
