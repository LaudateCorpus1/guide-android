package guide.graphql.toc.ui.sections

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import guide.graphql.toc.SectionsQuery
import guide.graphql.toc.data.apolloClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class SectionsViewModel : ViewModel() {

    private val _chapterId: MutableLiveData<Int> = MutableLiveData()

    var chapterId: Int
        get() {
            return _chapterId.value ?: -1
        }
        set(value) {
            if (value != _chapterId.value) {
                _chapterId.value = value
            }
        }

    val sectionException: MutableLiveData<Throwable> = MutableLiveData()

    val sectionList = _chapterId.switchMap { chapterId ->
        apolloClient.query(SectionsQuery(id = chapterId))
            .responseFetcher(ApolloResponseFetchers.CACHE_AND_NETWORK).watcher().toFlow()
            .map { response ->
                if (response.hasErrors()) throw Exception("Response has errors")
                val sections = response.data?.chapter?.sections ?: throw Exception("Data is null")
                if (sections.size > 1) {
                    return@map sections
                }
                throw Exception("No sections")
            }.catch { exception ->
                sectionException.postValue(exception)
            }.asLiveData()
    }
}