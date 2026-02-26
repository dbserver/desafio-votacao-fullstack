import { TbListDetails, TbMenu2 } from "react-icons/tb";
import { Button } from "@heroui/button";
import {
  Drawer,
  DrawerBody,
  DrawerContent,
  DrawerHeader,
} from "@heroui/drawer";
import { useState } from "react";
import { Link } from "@heroui/link";
import { useRouter } from "next/router";
import clsx from "clsx";

export default function Menu() {
  const [isOpen, setIsOpen] = useState(false);
  const { pathname } = useRouter();

  return (
    <>
      <Button
        isIconOnly
        size={"md"}
        type={"button"}
        variant={"flat"}
        onPress={() => {
          setIsOpen(true);
        }}
      >
        <TbMenu2 size={18} />
      </Button>
      <Drawer
        isOpen={isOpen}
        placement={"left"}
        onClose={() => setIsOpen(false)}
      >
        <DrawerContent>
          {() => (
            <>
              <DrawerHeader>
                <h4>Desafio Votação</h4>
              </DrawerHeader>
              <DrawerBody>
                <Link
                  className={clsx({
                    "dark:bg-[rgba(0,0,0,0.3)] bg-[rgba(0,0,0,0.1)] rounded-md p-2":
                      pathname.includes("/profile/rulling/"),
                  })}
                  href={"/profile/running/1"}
                  size={"lg"}
                >
                  <div
                    className={
                      "flex items-center gap-2 dark:text-white text-black"
                    }
                  >
                    <TbListDetails size={22} />
                    <span>Pautas</span>
                  </div>
                </Link>
              </DrawerBody>
            </>
          )}
        </DrawerContent>
      </Drawer>
    </>
  );
}
