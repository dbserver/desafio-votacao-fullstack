import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { PollService } from "../services/poll.service"

function Login() {

    const [loading, setLoading] = useState(false)

    const [document, setDocument] = useState('')
    const [error, setError] = useState('')

    const navigate = useNavigate()

    function handlePage(path: string) {
        navigate(path, { replace: true })
    }

    async function login() {
        setLoading(true)
        setError('')
        try {
            await PollService.validateDocument(document);
            localStorage.setItem('document', document)
            navigate('/')
        } catch (error) {
            setError('documento invalido')
        } finally {
            setLoading(false)
        }


        // PollService.create({
        //     "title": payload.title,
        //     "durationMinutes": payload.durationMinutes
        // }).then(() => {
        //     navigate('/')
        // })
        //     .finally(() => {
        //         setLoading(false)
        //     })
    }

    return (
        <div className="container-form-poll">
            {error && <p>{error}</p>}
            <div className="form-control">
                <label>CPF:</label>
                <input onChange={(evt) => setDocument(evt.target.value)} />
            </div>
            <button disabled={loading} onClick={() => login()}>
                Entrar
            </button>
        </div>
    )
}

export default Login