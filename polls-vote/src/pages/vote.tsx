import {useEffect, useState} from "react";
import {PollService} from "../services/poll.service.ts";
import {Link, useNavigate, useParams} from "react-router-dom";

function Vote() {

    const [poll, setPoll] = useState({ title: "" })
    const [vote, setVote] = useState("")
    const [confirm, setConfirm] = useState<boolean>(false)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string>('')
    const [success, setSuccess] = useState<boolean>(false)

    const { id } = useParams<{ id: string }>();

    const navigate = useNavigate();

    useEffect(() => {
        console.log('id', id);
        PollService.findById(id!)
            .then(res => {
                setPoll(res.data)
            })
    }, [])

    const handleSubmit = (value: string) => {
        setVote(value)
        setConfirm(true)
    }

    const handleVote = async () => {
        setLoading(true)

        try {
            const document = localStorage.getItem("document")
            await PollService.vote({ pollId: id, document, answer: vote })

            setSuccess(true)
        } catch (error) {
            console.log(error)
            setError(error?.response?.data?.message || 'Something went wrong')
            setConfirm(false)
        } finally {
            setLoading(false)
        }
    }

    if(success) {
        return <div className="container-vote">
            <p>Voto realizado com sucesso!</p>
            <button onClick={() => navigate('/vote')}>Ir para Pautas</button>
        </div>
    }

    return (
        <div className="container-vote">
            {
                error && (
                    <div>
                        <p>{error}</p>

                        <Link to="/vote">Ir para pautas</Link>
                    </div>
                )
            }


            <p>Pauta: { poll.title }</p>

            {
                !confirm && (
                    <div className="vote-btn">
                        <button onClick={() => handleSubmit('YES')}>SIM</button>
                        <button onClick={() => handleSubmit('NO')}>NÃO</button>
                    </div>
                )
            }

            {
                confirm && (
                    <div className="vote-btn">
                        <button disabled={loading} onClick={() => handleVote()}>Confirmar Voto: {vote}</button>
                    </div>
                )
            }

        </div>
    )
}

export default Vote