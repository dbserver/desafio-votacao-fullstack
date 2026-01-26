import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { PollService } from "../services/poll.service"

function Poll() {

    const [success, setSuccess] = useState(false)
    const [loading, setLoading] = useState(false)

    const [payload, setPayload] = useState({ title: '', durationMinutes: 1 })

    const navigate = useNavigate()

    function handlePage(path: string) {
        navigate(path, { replace: true })
    }

    function create() {

        setLoading(true)

        PollService.create({
            "title": payload.title,
            "durationMinutes": payload.durationMinutes
        }).then(() => {
            setSuccess(true)
        })
        .finally(() => {
            setLoading(false)
        })
    }

    if (success) {
        return (
            <div className="container-form-poll">
                <p>Pauta criada com sucesso!</p>
                <button onClick={() => handlePage('/vote')}>
                    Votar
                </button>
            </div>
        )
    }

    return (
        <div className="container-form-poll">
            <div className="form-control">
                <label>Titulo</label>
                <input value={payload.title} onChange={(evt) => setPayload((p) => ({ ...p, title: evt.target.value }))} />
            </div>

            <div className="form-control">
                <label>Tempo de duração da votação (em minutos)</label>
                <input
                    value={payload.durationMinutes}
                    type="number"
                    defaultValue={60}
                    onChange={(evt) => setPayload((p) => ({ ...p, durationMinutes: Number(evt.target.value) }))}
                />
            </div>
            <button disabled={loading} onClick={() => create()}>
                Criar
            </button>
        </div>
    )
}

export default Poll