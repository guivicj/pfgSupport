package org.guivicj.support

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform