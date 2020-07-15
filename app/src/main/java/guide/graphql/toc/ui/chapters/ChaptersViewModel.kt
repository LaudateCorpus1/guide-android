package guide.graphql.toc.ui.chapters

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import guide.graphql.toc.ChaptersQuery
import guide.graphql.toc.data.Resource
import guide.graphql.toc.data.apolloClient

class ChaptersViewModel : ViewModel() {

    val chapterList: LiveData<Resource<List<ChaptersQuery.Chapter>>> = liveData {
        emit(Resource.loading(null))
        try {
            val response = apolloClient.query(
                ChaptersQuery()
            ).toDeferred().await()

            if (response.hasErrors()) {
                throw Exception("Response has errors")
            }

            val chapters = response.data?.chapters ?: throw Exception("Data is null")
            emit(Resource.success(chapters))
        } catch (e: ApolloException) {
            emit(Resource.error("GraphQL request failed", null))
        } catch (e: Exception) {
            emit(Resource.error(e.message.orEmpty(), null))
        }
    }
}