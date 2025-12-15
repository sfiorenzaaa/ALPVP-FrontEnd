<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
package com.shannon.shannonweek14.data.service

import com.shannon.shannonweek14.data.dto.*
=======
package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
>>>>>>> Stashed changes
import retrofit2.Response
import retrofit2.http.*

interface EventService {

    // Public
    @GET("events")
    suspend fun getPublicEvents(): Response<EventListResponse>

    // Private
    @POST("events")
    suspend fun createEvent(
        @Body request: CreateEventRequest
    ): Response<EventResponse>

    // Private
    @GET("events/my")
    suspend fun getMyEvents(): Response<EventListResponse>

    // Admin only
    @PATCH("events/{id}")
    suspend fun updateEventStatus(
        @Path("id") eventId: Int,
        @Body request: UpdateEventStatusRequest
    ): Response<EventResponse>
}