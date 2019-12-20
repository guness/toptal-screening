package com.guness.toptal.server.repositories

import com.guness.toptal.server.model.Auth
import org.springframework.data.repository.CrudRepository

interface AuthRepository : CrudRepository<Auth, Long> {

}