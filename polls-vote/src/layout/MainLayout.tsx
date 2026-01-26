import {Outlet, useNavigate} from "react-router-dom";

export function MainLayout() {

    const navigate = useNavigate();

    const exit = () => {
        localStorage.clear()
        navigate("/");
    }

    return (
    <>
        {
            localStorage.getItem('document') && (
                <div className="header-actions">
                    <button onClick={() => navigate('/')}>Home</button>
                    <button onClick={() => exit()}>Sair</button>
                </div>
            )
        }
      <h1>Sistema Votação</h1>
      <Outlet />
      {/* <footer>Footer</footer> */}
    </>
  );
}
