package com.example.mobile

import java.time.LocalDate
import java.time.LocalTime

data class Reservas (
    val IDReserva: Int,
    val IDSala: Int,
    val IDUtilizador: Int,
    val NomeReserva: String,
    var DataReserva: LocalDate,
    var NParticipantes: Int,
    var HoraInicio: LocalTime,
    var HoraFim : LocalTime,
    var IDEstado:Int,
    var NomeSala:String
)