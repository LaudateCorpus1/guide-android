package guide.graphql.toc.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory

object Apollo {
    val client: ApolloClient by lazy {
        val cacheFactory =
            LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024 * 1024).build())

        ApolloClient.builder()
            .serverUrl("https://api.graphql.guide/graphql")
            .normalizedCache(cacheFactory)
            .build()
    }
}
