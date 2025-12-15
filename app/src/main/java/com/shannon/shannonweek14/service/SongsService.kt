import com.shannon.shannonweek14.data.model.SongsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Body
import retrofit2.http.POST

interface SongsService {

    // GET quiz tanpa token
    @GET("songs")
    suspend fun getSongsQuiz(): Response<SongsResponse>

    // GET quiz dengan Bearer token
    @GET("songs")
    suspend fun getSongsQuizAuth(
        @Header("Authorization") authHeader: String
    ): Response<SongsResponse>

    // POST jawaban quiz
    @POST("songs")
    suspend fun postSongAnswer(
        @Body body: Map<String, Any>,
        @Header("Authorization") authHeader: String
    ): Response<Any>
}
