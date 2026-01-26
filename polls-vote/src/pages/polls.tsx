import {useEffect, useState} from "react";
import {PollService} from "../services/poll.service.ts";
import {useNavigate} from "react-router-dom";

function PollsList() {

    const [polls, setPolls] = useState([])

    useEffect(() => {
        PollService.list()
            .then(res => {
                console.log(res.data.content)
                setPolls(res.data.content)
            })
    }, [])
    
    const navigate = useNavigate();

    const handleClick = (id: number) => {
        navigate(`/vote/${id}`)
    }

    return (
        <div>
            <p>Pautas para votação</p>
            <div className="poll-list">
                {polls.map((poll, index) => (
                    <div key={index} className="poll-list-item">
                        {poll.title}
                        <button onClick={() => handleClick(poll.id)}>Votar</button>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default PollsList