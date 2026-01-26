import { useNavigate } from "react-router-dom"

function Home() {

    const navigate = useNavigate()

    function handlePage(path: string) {
        navigate(path, { replace: true })
    }

    return (
        <div className="container-enter-button card">
            <button onClick={() => handlePage('/polls')}>
                Criar Pauta
            </button>
            <button onClick={() => handlePage('/vote')}>
                Votar
            </button>
        </div>
    )
}

export default Home