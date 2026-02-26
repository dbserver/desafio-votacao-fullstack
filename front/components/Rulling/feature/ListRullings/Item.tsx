import { Progress } from "@heroui/progress";
import { Button } from "@heroui/button";
import clsx from "clsx";
import { useState } from "react";

import { Rulling } from "@/domain/Rulling/Model/Rulling";
import textLimiter from "@/commons/helpers/textLimiter";
import VoteDrawer from "@/components/Rulling/feature/ListRullings/VoteDrawer";

type Props = {
  item: Rulling;
  refetch?: VoidFunction;
};
export default function Item({ item, refetch }: Props) {
  const [open, setOpen] = useState<boolean>(false);

  return (
    <>
      <div
        key={item.id}
        className={
          "dark:bg-[rgba(255,255,255,0.1)] bg-[rgba(0,0,0,0.02)] rounded-md p-1 h-[300px] grid grid-cols-1 grid-rows-[1fr_1fr_1fr] gap-2 items-center"
        }
      >
        <header className={"p-2"}>
          <h5 className={"text-sm"}>{textLimiter(item.title, 38)}</h5>
          <span className={"text-xs font-light"}>
            Total de votos: {item.voteTotal}
          </span>
          {item.description && (
            <div className={"w-full pt-2 pb-1"}>
              <p className={"text-xs"}>{textLimiter(item.description, 46)}</p>
            </div>
          )}
        </header>
        <section className={"px-2 pb-2"}>
          <div className={"w-full pb-2 text-success"}>
            <Progress
              color={"success"}
              label={`Votos Sim: ${item.voteYes}`}
              size={"sm"}
              value={item.voteYes / (item.voteTotal * 0.01)}
            />
          </div>
          <div className={"w-full text-danger"}>
            <Progress
              color={"danger"}
              label={`Votos Não: ${item.voteNo}`}
              size={"sm"}
              value={item.voteNo / (item.voteTotal * 0.01)}
            />
          </div>
        </section>
        <footer className={"p-2 flex items-center gap-2"}>
          <Button
            className={clsx({
              "cursor-not-allowed grayscale": !item.active,
            })}
            color={"primary"}
            disabled={!item.active}
            size={"sm"}
            variant={"solid"}
            onPress={() => setOpen(true)}
          >
            Votar
          </Button>
          {!item.active && (
            <p className={"text-sm text-danger font-light"}>
              Votação encerrada!
            </p>
          )}
        </footer>
        {item.active && open && (
          <VoteDrawer
            key={item.id}
            isOpen={open}
            refetch={refetch}
            rulling={item}
            onClose={() => setOpen(false)}
          />
        )}
      </div>
    </>
  );
}
