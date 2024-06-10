package com.minhmdl.goodbooks.model

data class User(
    val displayName: String,
    val userID: String,
    val avatar: String,
    val shelves: List<Shelf>,
    val searchHistory: List<String>,
    val reviews: List<Review>,
    val favourites: List<Book>,
    val progressReading: List<progressReading>
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
    val book: Book,
    val rating: Double,
    val reviewText: String
){
    // Add a no-argument constructor
    constructor() : this(Book(), 0.0, "")
}

data class progressReading(
    val bookId: String,
    var progress: Int
){
    // Add a no-argument constructor
    constructor() : this("", 0)
}