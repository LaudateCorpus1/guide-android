package guide.graphql.toc.ui.sections

import android.util.Log
import androidx.lifecycle.*
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import guide.graphql.toc.Resource
import guide.graphql.toc.SectionsQuery
import guide.graphql.toc.apolloClient

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
        _chapterId.switchMap { sectionId ->
            return@switchMap liveData {
                emit(Resource.loading(null))
                try {
                    val response = apolloClient.query(
                        SectionsQuery(id = sectionId)
                    ).toDeferred().await()

                    if (response.hasErrors()) {
                        emit(Resource.error("Response has errors", null))
                        return@liveData
                    }
                    response.data?.chapter?.sections?.let { sections ->
                        if (sections.size > 1) {
                            emit(Resource.success(sections))
                        } else {
                            emit(Resource.error("No sections", null))
                        }
                        return@liveData
                    }
                    emit(Resource.error("Chapter has no sections", null))
                    return@liveData
                } catch (e: ApolloException) {
                    Log.d("Sections Query", "GraphQL request failed", e)
                    emit(Resource.error("GraphQL request failed", null))
                    return@liveData
                }
            }
        }
}