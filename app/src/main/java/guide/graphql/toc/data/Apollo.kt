package guide.graphql.toc.data

import com.apollographql.apollo.ApolloClient

object Apollo {
    val client: ApolloClient by lazy {
        ApolloClient.builder()
            .serverUrl("https://api.graphql.guide/graphql")
            .build()
    }
}