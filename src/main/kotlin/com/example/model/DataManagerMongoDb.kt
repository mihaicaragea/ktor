package com.example.model

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.or
import com.mongodb.client.model.Filters.regex
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import ui.login.Session

enum class DataManagerMongoDb {
  INSTANCE;
  val log = LoggerFactory.getLogger(DataManagerMongoDb::class.java)
  val dataBase: MongoDatabase
  val bookCollection: MongoCollection<Book>
  val cartCollection: MongoCollection<Cart>

  init {
    val pojoCodecRegistry: CodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build())
    val codecRegistry: CodecRegistry = fromRegistries(
      MongoClientSettings.getDefaultCodecRegistry(),
      pojoCodecRegistry
    )

    val clientSettings = MongoClientSettings.builder()
      .codecRegistry(codecRegistry)
      .build()

    val mongoClient = MongoClients.create(clientSettings)
    dataBase = mongoClient.getDatabase("development");
    bookCollection = dataBase.getCollection(Book::class.java.name, Book::class.java)
    cartCollection = dataBase.getCollection(Cart::class.java.simpleName, Cart::class.java)
    initBooks()
  }

  private fun initBooks() {
    bookCollection.insertOne(Book(null, "How to grow apples", "Mr. Appleton", 100.0f))
    bookCollection.insertOne(Book(null, "How to grow oranges", "Mr. Oranges", 90.0f))
    bookCollection.insertOne(Book(null, "How to grow lemons", "Mr. Lemons", 110.0f))
    bookCollection.insertOne(Book(null, "How to grow pineapples", "Mr. Pineapple", 100.0f))
    bookCollection.insertOne(Book(null, "How to grow pears", "Mr. Pears", 110.0f))
    bookCollection.insertOne(Book(null, "How to grow coconuts", "Mr. Coconuts", 130.0f))
    bookCollection.insertOne(Book(null, "How to grow bananas", "Mr. Appleton", 120.0f))
  }

  fun newBook(book: Book) : Book {
    bookCollection.insertOne(book)
    return book
  }

  fun updateBook(book: Book): Book? {
    return bookCollection.find(Document("_id", book.id)).first()?.apply {
      title = book.title
      author = book.author
      price = book.price
    }
  }

  fun deleteBook(bookId: String): Book? {
    val foundBook = bookCollection.find(Document("_id", bookId)).first()
    bookCollection.deleteOne(eq("_id", ObjectId(bookId)))
    return foundBook
  }


  fun allBooks(): List<Book> {
    return bookCollection.find().toList()
  }

  fun sortedBooks(sortBy: String, asc: Boolean): List<Book> {
    val pageNr = 1
    val pageSize = 1000
    val ascInt: Int = if (asc) 1 else -1

    return bookCollection.find()
      .sort(Document(mapOf(sortBy to ascInt, "_id" to -1)))
      .skip(pageNr - 1)
      .limit(pageSize)
      .toList()
  }

  fun searchBooks(str: String): List<Book> {
    return bookCollection
      .find(
        or(
          regex("title", ".*$str.*"),
          regex("author", ".*$str.*")
        )
      )
      .sort(Document(mapOf(Pair("title", 1), Pair("_id", -1))))
      .toList()
  }
  fun updateCart(cart:Cart){
    val replaceOne = cartCollection.replaceOne(eq("username", cart.username), cart)
    log.info("Update result: $replaceOne")
  }

  fun addBook(session: Session?, book: Book){
    val cartForUser = cartForUser(session)
    cartForUser.addBook(book)
    updateCart(cartForUser)
  }

  fun cartForUser(session: Session?): Cart{
    if (session == null)
      throw IllegalArgumentException("Session is null")
    val find = cartCollection.find(eq("username", session.username))

    if (find.count() == 0){
      val cart = Cart(username=session.username)
      cartCollection.insertOne(cart)
      return cart
    }
    else
      return find.first()
  }

  fun getBookWithId(bookid: String): Book {
    log.info("Get book with id: $bookid")
    return bookCollection.find(eq("_id", ObjectId(bookid))).first()
  }

  fun removeBook(session: Session?, book: Book) {
    val cartForUser = cartForUser(session)
    cartForUser.removeBook(book)
    updateCart(cartForUser)
  }
}