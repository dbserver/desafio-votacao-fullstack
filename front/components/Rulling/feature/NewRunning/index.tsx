import { Button } from "@heroui/button";
import { TbCirclePlus } from "react-icons/tb";
import { Modal, ModalBody, ModalContent, ModalHeader } from "@heroui/modal";
import { useState } from "react";

import NewRullingForm from "@/components/Rulling/feature/NewRunning/NewRullingForm";

type Props = {
  refetch?: VoidFunction;
};

export default function NewRunning({ refetch }: Props) {
  const [isOpen, setIsOpen] = useState<boolean>(false);

  return (
    <>
      <Button
        color={"success"}
        size={"md"}
        startContent={<TbCirclePlus size={20} />}
        variant={"flat"}
        onPress={() => setIsOpen(true)}
      >
        Nova Pauta
      </Button>
      <Modal isOpen={isOpen} size={"lg"} onClose={() => setIsOpen(false)}>
        <ModalContent>
          {() => (
            <>
              <ModalHeader>
                <h3>E Qual é a Pauta?</h3>
              </ModalHeader>
              <ModalBody>
                <NewRullingForm
                  fallback={() => {
                    setIsOpen(false);
                    refetch && refetch();
                  }}
                />
              </ModalBody>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}
