package com.sssakib.mobilerechargecloneapp


data class Contact(

    var id: Int = 0,
    var name: String?,
    var number: String?

)
{
    constructor(name: String?,number: String?) : this(0,name,number)

}