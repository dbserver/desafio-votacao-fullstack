import RestConsumer from './RestConsumer';

const VoteService = {
    applyVote: (vote) =>
        RestConsumer.post(
            '/vote',
            vote,
            {
                'Content-Type': 'application/json'
            }
        ),

    getVoteStatus: (vote) =>
        RestConsumer.post(`/voteStatus`,
            vote, 
            {
                'Content-Type': 'application/json',
            }),
};

export default VoteService;