package com.cainpvp.vigilante.core

abstract class Controller(val model: Model, val view: View) {
    protected val subControllers = mutableListOf<Controller>()
}