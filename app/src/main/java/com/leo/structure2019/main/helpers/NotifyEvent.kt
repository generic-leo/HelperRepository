package com.leo.homeloan.main.helpers

class NotifyEvent{
    private var type = 0
    private var subType = 0
    private var stringValue: String = ""
    private var intValue: Int = 0

    private var stringVarArgsValue: String? = null
    private var intVarArgsValue: IntArray = intArrayOf()

    private var keyValue: HashMap<String, String>? = null
    private var hasBeenHandled = false

    constructor(type: Int, subType: Int, stringContent: String){
        this.type = type
        this.subType = subType
        this.stringValue = stringContent
    }

    constructor(type: Int, subType: Int, intContent: Int){
        this.type = type
        this.subType = subType
        this.intValue = intContent
    }

    constructor(type: Int, subType: Int, intContent: Int, stringContent: String){
        this.type = type
        this.subType = subType
        this.intValue = intContent
        this.stringValue = stringContent
    }

    constructor(type: Int, subType: Int, stringContent: String, vararg intVarArgsValue: Int ){
        this.type = type
        this.subType = subType
        this.stringValue = stringContent
        this.intVarArgsValue = intVarArgsValue
    }

    constructor(type: Int, subType: Int, content: HashMap<String, String>){
        this.type = type
        this.subType = subType
        this.keyValue = content
    }

    fun isContentHandled()= hasBeenHandled

    /**
     * Get the type of Event
     */
    fun type() = type
    fun subType() = subType
    fun getValue(): String? {
        hasBeenHandled = true
        return stringValue
    }
    fun getIntValue(): Int {
        hasBeenHandled = true
        return intValue
    }
    fun getIntValues(): IntArray{
        hasBeenHandled = true
        return intVarArgsValue
    }


    fun getValue(key: String, defaultValue: String=""): String {
        hasBeenHandled = true
        keyValue?.get(key).let {
            return it!!
        }

        return defaultValue
    }

    fun peekStringValue() = stringValue
    fun peekintValue() = intValue
    fun peekValues() = keyValue
}