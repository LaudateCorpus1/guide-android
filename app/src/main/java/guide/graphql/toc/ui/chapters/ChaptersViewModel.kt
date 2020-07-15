package guide.graphql.toc.ui.chapters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import guide.graphql.toc.ChaptersQuery
import guide.graphql.toc.data.apolloClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class ChaptersViewModel : ViewModel() {

    val chapterException: MutableLiveData<Throwable?> = MutableLiveData()

    val chapterList = apolloClient.query(ChaptersQuery())
        .responseFetcher(ApolloResponseFetchers.CACHE_AND_NETWORK).watcher().toFlow().map { response ->
            if (response.hasErrors()) throw Exception("Response has errors")
            val chapters = response.data?.chapters ?: throw Exception("Data is null")
            chapterException.value = null
            return@map chapters
        }.catch { exception ->
            chapterException.postValue(exception)
        }.asLiveData()
}