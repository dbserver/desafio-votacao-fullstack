import {
  Drawer,
  DrawerBody,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
} from "@heroui/drawer";
import { Button } from "@heroui/button";
import { useEffect } from "react";

import { Rulling } from "@/domain/Rulling/Model/Rulling";
import dateTimeString from "@/commons/helpers/dateTimeString";
import useApiHookVoteYes from "@/domain/Rulling/Api/useApiHookVoteYes";
import useApiHookVoteNo from "@/domain/Rulling/Api/useApiHookVoteNo";

type Props = {
  isOpen: boolean;
  onClose: VoidFunction;
  refetch?: VoidFunction;
  rulling: Rulling;
};

export default function VoteDrawer({
  isOpen,
  onClose,
  rulling,
  refetch,
}: Props) {
  const {
    response: yesResponse,
    fetch: voteYes,
    isLoading: isLoadingYesVote,
  } = useApiHookVoteYes();

  const {
    response: noResponse,
    fetch: voteNo,
    isLoading: isLoadingNoVote,
  } = useApiHookVoteNo();

  useEffect(() => {
    if (yesResponse || noResponse) {
      refetch && refetch();
      onClose();
    }
  }, [yesResponse, noResponse]);

  return (
    <>
      <Drawer
        backdrop={"blur"}
        isOpen={isOpen}
        placement={"right"}
        shadow={"sm"}
        size={"2xl"}
        onClose={() => {
          if (isLoadingYesVote || isLoadingNoVote) return;
          onClose();
        }}
      >
        <DrawerContent>
          {() => (
            <>
              <DrawerHeader>
                <header className="w-dull">
                  <div className={"w-full"}>
                    <h3>{rulling.title}</h3>
                  </div>
                  <p className={"px-2 py-4 text-sm font-light"}>
                    {rulling.description}
                  </p>
                </header>
              </DrawerHeader>
              <DrawerBody>
                <div className={"py-1"}>
                  <h5>Você vota:</h5>
                </div>
                <section className="w-full flex gap-2 items-center">
                  <Button
                    color={"success"}
                    isLoading={isLoadingYesVote || isLoadingNoVote}
                    variant={"flat"}
                    onPress={() => {
                      voteYes(rulling.id);
                    }}
                  >
                    SIM
                  </Button>
                  <span>ou</span>
                  <Button
                    color={"danger"}
                    isLoading={isLoadingYesVote || isLoadingNoVote}
                    variant={"flat"}
                    onPress={() => {
                      voteNo(rulling.id);
                    }}
                  >
                    NÃO
                  </Button>
                </section>
              </DrawerBody>
              <DrawerFooter>
                <p className={"text-danger"}>
                  Essa pauta expira em: {dateTimeString(rulling.expiration)}
                </p>
              </DrawerFooter>
            </>
          )}
        </DrawerContent>
      </Drawer>
    </>
  );
}
