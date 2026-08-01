import axios from 'axios';

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

apiClient.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 400) {
            return Promise.reject({
                type: "VALIDATION_ERROR",
                errors: error.response.data,
            });
        }

        return Promise.reject(error);
    }
);

export default apiClient;