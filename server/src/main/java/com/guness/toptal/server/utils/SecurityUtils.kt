package com.guness.toptal.server.utils

import com.guness.toptal.protocol.dto.UserRole
import com.guness.toptal.server.model.Authority
import org.springframework.security.core.context.SecurityContextHolder

fun Array<UserRole>.hasAll() = roles().containsAll(asList())

fun Array<UserRole>.hasAny() = roles().any(::contains)

fun roles() = SecurityContextHolder.getContext()
    .authentication.authorities
    .map { (it as Authority).role }