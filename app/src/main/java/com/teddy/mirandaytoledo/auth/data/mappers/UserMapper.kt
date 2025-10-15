package com.teddy.mirandaytoledo.auth.data.mappers

import com.teddy.mirandaytoledo.auth.domain.User
import com.teddy.mirandaytoledo.auth.data.dto.UserDto

fun User.toDto(): UserDto{
    return UserDto(usuario = username, contra = password)
}