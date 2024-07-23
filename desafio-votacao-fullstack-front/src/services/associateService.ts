import axios from "axios";
import { Associate } from "../pages/Associate/AssociateTable";

const apiUrl = import.meta.env.VITE_APP_API_URL;

export const findAllAssociates = async (): Promise<Associate[]> => {
  const response = await axios.get<Associate[]>(`${apiUrl}/api/associate`);
  return response.data;
};

export const createAssociate = async (associate: Omit<Associate, 'id'>): Promise<void> => {
  await axios.post(`${apiUrl}/api/associate`, associate);
};

export const updateAssociate = async (id: number, associate: Omit<Associate, 'id'>): Promise<void> => {
  await axios.put(`${apiUrl}/api/associate/${id}`, associate);
};
