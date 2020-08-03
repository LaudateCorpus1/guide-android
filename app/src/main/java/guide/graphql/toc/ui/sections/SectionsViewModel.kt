package guide.graphql.toc.ui.sections

import androidx.lifecycle.*
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import guide.graphql.toc.SectionsQuery
import guide.graphql.toc.data.Apollo
import guide.graphql.toc.data.Resource

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

    val sectionsList: LiveData<Resource<List<SectionsQuery.Section?>>> =
        _chapterId.switchMap { chapterId ->
            return@switchMap liveData {
                emit(Resource.loading(null))
                try {
                    val response = Apollo.client.query(
                        SectionsQuery(id = chapterId)
                    ).toDeferred().await()

                    if (response.hasErrors()) {
                        throw Exception("Response has errors")
                    }
                    val sections = response.data?.chapter?.sections ?: throw Exception("Data is null")
                    if (sections.size > 1) {
                        emit(Resource.success(sections))
                    } else {
                        throw Exception("No sections")
                    }
                } catch (e: ApolloException) {
                    emit(Resource.error("GraphQL request failed", null))
                } catch (e: Exception) {
                    emit(Resource.error(e.message.orEmpty(), null))
                }
            }
        }
}