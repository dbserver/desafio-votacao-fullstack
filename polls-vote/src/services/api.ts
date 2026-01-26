import axios from "axios";

export const api = axios.create({
  baseURL: 'http://localhost:8080', // ou process.env.REACT_APP_API_URL
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});
