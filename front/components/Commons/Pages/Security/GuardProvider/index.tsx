"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/router";
import { Spinner } from "@heroui/spinner";

import TComponentGenericProps from "@/commons/types/TComponentGenericProps";
import { AuthService } from "@/domain/User/Services/Auth.service";

export default function GuardProvider({
  children,
}: TComponentGenericProps<unknown>) {
  const [hasSession, setHasSession] = useState(false);
  const { push } = useRouter();

  useEffect(() => {
    const token = AuthService.getToken();

    if (token) {
      setHasSession(true);
    } else {
      push("/");
    }
  }, []);

  if (!hasSession)
    return (
      <section
        className={"h-[100vh] w-[100vw] flex items-center justify-center"}
      >
        <Spinner size={"lg"} />
      </section>
    );

  return <>{children}</>;
}
