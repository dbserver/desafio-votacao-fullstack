package com.service.polls.config;

import com.service.polls.application.usecases.*;
import com.service.polls.ports.in.*;
import com.service.polls.ports.out.IPollRepository;
import com.service.polls.ports.out.IVoteCacheProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ContainerBindConfig {

    @Bean
    ICreatePollUseCase createPollUseCase(IPollRepository pollRepository) {
        return new CreatePollUseCase(pollRepository);
    }

    @Bean
    IListPollsUseCase listPollsUseCase(IPollRepository pollRepository) {
        return new ListPollUseCase(pollRepository);
    }

    @Bean
    IVoteUseCase voteUseCase(IPollRepository pollRepository, IVoteCacheProvider cacheProvider) {
        return new VoteUseCase(pollRepository, cacheProvider);
    }

    @Bean
    IGetPollsUseCase getPollsUseCase(IPollRepository pollRepository) {
        return new GetPollUseCase(pollRepository);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    IGetPollResultUseCase pollResultUseCase(IVoteCacheProvider cacheProvider) {
        return new GetPollResultUseCase(cacheProvider);
    }
}
