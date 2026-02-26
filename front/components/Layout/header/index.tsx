"use client";
import { Button } from "@heroui/button";
import { TbLogout } from "react-icons/tb";
import { useEffect, useState } from "react";
import { useRouter } from "next/router";

import ThemeButton from "@/components/Commons/UI/ThemeButton";
import { AuthService } from "@/domain/User/Services/Auth.service";
import Menu from "@/components/Layout/header/Menu";

export default function Header() {
  const [hasToken, setHasToken] = useState<boolean>(false);
  const { push } = useRouter();

  useEffect(() => {
    const token = AuthService.getToken();

    if (token) setHasToken(true);
  }, []);

  return (
    <header
      className={"w-full grid grid-cols-[auto_1fr] py-1 items-center gap-2"}
    >
      <div className={"flex items-center gap-2"}>
        {hasToken && <Menu />}
        <h1 className={"text-xl font-bold"}>Desafio Votação</h1>
      </div>

      <section className={"flex justify-end gap-1 items-center"}>
        <ThemeButton />
        {hasToken && (
          <Button
            isIconOnly
            color={"danger"}
            size={"md"}
            type={"button"}
            variant={"flat"}
            onPress={() => {
              AuthService.logout();
              push("/");
            }}
          >
            <TbLogout size={20} />
          </Button>
        )}
      </section>
    </header>
  );
}
