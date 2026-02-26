import Axios, { AxiosInstance } from "axios";

import { AuthService } from "@/domain/User/Services/Auth.service";

export default function ApiClient(): AxiosInstance {
  const token = AuthService.getToken();
  const newClient = Axios.create({
    baseURL: `http://${process.env.NEXT_PUBLIC_API_HOST}:${process.env.NEXT_PUBLIC_API_HOST_PORT}`,
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (token)
    newClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;

  return newClient;
}
