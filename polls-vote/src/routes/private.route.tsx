import { Navigate } from "react-router-dom";

export function PrivateRoute({ children }: { children: JSX.Element }) {
    const isAuthenticated = localStorage.getItem("document");

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    return children;
}
