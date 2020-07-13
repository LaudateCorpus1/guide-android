package guide.graphql.toc.ui.chapters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import guide.graphql.toc.ChaptersQuery
import guide.graphql.toc.Resource
import guide.graphql.toc.apolloClient

class ChaptersViewModel : ViewModel() {

    val bookId: LiveData<Int> = MutableLiveData(0)

    val chapterList: LiveData<Resource<List<ChaptersQuery.Chapter>>> = liveData {
        emit(Resource.loading(null))
        try {
            val response = apolloClient.query(
                ChaptersQuery()
            ).toDeferred().await()

            if (response.hasErrors()) {
                emit(Resource.error("Response has errors" ,null))
            }
            if (response.data?.chapters != null) {
                emit(Resource.success(response.data!!.chapters))
            } else {
                emit(Resource.error("Data is null" ,null))
            }
        } catch (e: ApolloException) {
            Log.d("ChaptersQuery", "GraphQL request failed", e)
             emit(Resource.error("GraphQL request failed", null))
        }
    }
}