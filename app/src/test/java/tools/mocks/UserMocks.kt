package tools.mocks

import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.dto.UserRole
import tools.TestUtils

val user1 = User(
    id = TestUtils.id,
    name = "Sinan Gunes - User",
    username = "user",
    role = UserRole.ROLE_USER
)

val manager1 = User(
    id = TestUtils.id,
    name = "Sinan Gunes - Manager",
    username = "manager",
    role = UserRole.ROLE_MANAGER
)

val admin1 = User(
    id = TestUtils.id,
    name = "Sinan Gunes - Admon",
    username = "admin",
    role = UserRole.ROLE_ADMIN
)