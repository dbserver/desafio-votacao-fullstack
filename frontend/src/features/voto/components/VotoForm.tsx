import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { Button } from "../../../components/ui/Button";
import { Form, FormActions } from "../../../components/ui/Form";
import { TextField } from "../../../components/ui/TextField";
import { formatarCpf } from "../../../lib/format";
import { cpfMask, withMask } from "../../../lib/mask";
import { useBuscarAssociadoPorCpf } from "../../associado/hooks";
import {
  buscarPorCpfSchema,
  type AssociadoResponse,
  type BuscarPorCpf,
} from "../../associado/schema";
import { useVotar } from "../hooks";
import type { OpcaoVoto } from "../schema";
import "./VotoForm.css";

type VotoFormProps = {
  pautaId: number;
};

export const VotoForm = ({ pautaId }: VotoFormProps) => {
  const [associado, setAssociado] = useState<AssociadoResponse | null>(null);

  const buscarAssociado = useBuscarAssociadoPorCpf();
  const votar = useVotar(pautaId);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<BuscarPorCpf>({
    resolver: zodResolver(buscarPorCpfSchema),
    defaultValues: { cpf: "" },
  });

  const { onChange, ...cpf } = register("cpf");

  const onBuscar = handleSubmit(async ({ cpf }) => {
    try {
      setAssociado(await buscarAssociado.mutateAsync(cpf));
    } catch (erro) {
      toast.error(
        erro instanceof Error
          ? erro.message
          : "Não foi possível buscar o associado",
      );
    }
  });

  const onVotar = async (opcao: OpcaoVoto) => {
    if (!associado) {
      return;
    }

    try {
      await votar.mutateAsync({ associadoId: associado.id, opcao });

      toast.success(`Voto ${opcao} registrado`);
      trocarAssociado();
    } catch (erro) {
      toast.error(
        erro instanceof Error ? erro.message : "Não foi possível votar",
      );
    }
  };

  const trocarAssociado = () => {
    setAssociado(null);
    reset();
  };

  if (!associado) {
    return (
      <Form onSubmit={onBuscar}>
        <TextField
          label="CPF do associado"
          placeholder="529.982.247-25"
          inputMode="numeric"
          error={errors.cpf?.message}
          onChange={withMask(cpfMask, onChange)}
          {...cpf}
        />

        <FormActions>
          <Button type="submit" loading={buscarAssociado.isPending}>
            Buscar associado
          </Button>
        </FormActions>
      </Form>
    );
  }

  return (
    <div className="voto-form">
      <p className="voto-form__associado">
        Votando como <strong>{associado.nome}</strong>
        <span className="voto-form__cpf">{formatarCpf(associado.cpf)}</span>
      </p>

      <FormActions>
        <Button
          type="button"
          onClick={() => onVotar("SIM")}
          loading={votar.isPending}
        >
          Votar SIM
        </Button>

        <Button
          type="button"
          variant="secondary"
          onClick={() => onVotar("NAO")}
          loading={votar.isPending}
        >
          Votar NÃO
        </Button>
      </FormActions>

      <button
        className="voto-form__trocar"
        type="button"
        onClick={trocarAssociado}
      >
        Trocar associado
      </button>
    </div>
  );
};
