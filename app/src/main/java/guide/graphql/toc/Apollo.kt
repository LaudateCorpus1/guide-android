package guide.graphql.toc

import com.apollographql.apollo.ApolloClient

val apolloClient: ApolloClient = ApolloClient.builder()
    .serverUrl("https://api.graphql.guide/graphql")
    .build()