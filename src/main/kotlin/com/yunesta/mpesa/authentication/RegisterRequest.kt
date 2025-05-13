package com.yunesta.mpesa.authentication;

data class RegisterRequest(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var status: String = ""
)