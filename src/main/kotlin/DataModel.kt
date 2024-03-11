import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

class Book(
   id: ObjectId?,
   title: String,
   author:String,
   price: Float) {

  @BsonId
  val id: ObjectId?
  var title: String
  var author: String
  var price: Float

  constructor() : this (null, "not_set", "not_set", 0.00f)

  init {
    this.id = id
    this.title = title
    this.author = author
    this.price = price
  }
}
data class ShoppingCarat(var id: String, var userId: String, val items: ArrayList<ShoppingItem>)
data class ShoppingItem(var bookId: String, var qty: Int)
data class User(var id: String, var username: String, var password:String)
