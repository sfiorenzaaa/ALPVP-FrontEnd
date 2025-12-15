<<<<<<< Updated upstream
<<<<<<< Updated upstream
package com.shannon.shannonweek14.data.dto

import com.shannon.shannonweek14.data.model.Event
=======
package com.shannon.shannonweek14.dto

import com.shannon.shannonweek14.ui.model.Event
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.dto

import com.shannon.shannonweek14.ui.model.Event
>>>>>>> Stashed changes

data class CreateEventRequest(
    val title: String,
    val description: String,
    val date: String
)

data class UpdateEventStatusRequest(
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    val status: String // "APPROVE" atau "REJECT"
=======
    val status: String
>>>>>>> Stashed changes
=======
    val status: String
>>>>>>> Stashed changes
)

data class EventResponse(
    val data: Event
)

data class EventListResponse(
    val data: List<Event>
)