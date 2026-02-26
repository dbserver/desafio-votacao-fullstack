"use client";
import { AxiosResponse } from "axios";

import TUserLogin from "@/domain/User/Types/TUserLogin";
import TUserAuthToken from "@/domain/User/Types/TUserAuthToken";
import ApiClient from "@/commons/clients/ApiClient";
import TUserSignup from "@/domain/User/Types/TUserSignup";
import { User } from "@/domain/User/Model/User";

export class AuthService {
  public static getToken(): string | null {
    return sessionStorage.getItem("user.session.token");
  }

  public static saveSession(token: string): void {
    sessionStorage.setItem("user.session.token", token);
  }

  public static logout(): void {
    sessionStorage.removeItem("user.session.token");
  }

  public static login(
    data: TUserLogin,
  ): Promise<AxiosResponse<TUserAuthToken>> {
    return ApiClient().post<TUserAuthToken>("/user/login", {
      email: data.email,
    });
  }

  public static register(data: TUserSignup): Promise<AxiosResponse<User>> {
    return ApiClient().post<User>("/user", data);
  }
}
