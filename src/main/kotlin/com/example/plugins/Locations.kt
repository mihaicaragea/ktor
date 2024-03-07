package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.locations.Locations

fun Application.configureLocations() {
  install(Locations) {

  }
}