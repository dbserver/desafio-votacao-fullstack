"use client";
import { Tab, Tabs } from "@heroui/tabs";
import { useRouter } from "next/router";
import { useEffect } from "react";

import Login from "@/components/HomePage/features/LoginLogoutForm/Login";
import Register from "@/components/HomePage/features/LoginLogoutForm/Register";
import { AuthService } from "@/domain/User/Services/Auth.service";

export default function LoginLogoutForm() {
  const { push } = useRouter();

  useEffect(() => {
    const token = AuthService.getToken();

    if (token) push("/profile/rulling/1");
  }, []);

  return (
    <div className={"w-[440px] p-2 border border-2 rounded-2xl"}>
      <Tabs aria-label="Options">
        <Tab key="login" title="Login">
          <Login />
        </Tab>
        <Tab key="signup" title="Cadastre-se">
          <Register />
        </Tab>
      </Tabs>
    </div>
  );
}
