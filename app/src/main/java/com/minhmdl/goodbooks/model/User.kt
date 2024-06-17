package com.minhmdl.goodbooks.model

import com.google.firebase.Timestamp
import java.time.LocalDate

data class User(
    val displayName: String,
    val userID: String,
    val avatar: String,
    val shelves: List<Shelf>,
    val searchHistory: List<String>,
    val reviews: List<Review>,
    val favourites: List<Book>,
    val progressReading: List<progressReading>,
    val dates: List<dateTime>
)
{
    // Add a no-argument constructor
    constructor() : this(
        "",
        "",
        "",
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf()
    )
}

data class Shelf(
    val name: String,
    var books: List<Book>
){
    // No-argument constructor required for Firestore deserialization
    constructor() : this("", emptyList())
}


data class Review(
    val bookId: String,
    var reviewText: String
){
    // Add a no-argument constructor
    constructor() : this("", "")
}

data class progressReading(
    val bookId: String,
    var progress: Int
){
    // Add a no-argument constructor
    constructor() : this("", 0)
}
data class dateTime(
    val bookId: String,
    var date: String
)
{
    // Add a no-argument constructor
    constructor() : this("", "")
}