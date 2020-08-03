package guide.graphql.toc

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory

class GuideApp : Application() {
    val apolloClient: ApolloClient by lazy {
        val cacheFactory =
            LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024 * 1024).build())

        ApolloClient.builder()
            .serverUrl("https://api.graphql.guide/graphql")
            .normalizedCache(cacheFactory)
            .build()
    }
}