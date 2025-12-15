<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
package com.shannon.shannonweek14.data.model
=======
package com.shannon.shannonweek14.ui.model
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.ui.model
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.ui.model
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.ui.model
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.ui.model
>>>>>>> Stashed changes
=======
package com.shannon.shannonweek14.ui.model
>>>>>>> Stashed changes

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val eventDate: String,
    val status: String, // "PENDING", "APPROVE", "REJECT"
    val createdAt: String,
    val userId: Int,
    val user: EventUser? = null
)

data class EventUser(
    val username: String
)