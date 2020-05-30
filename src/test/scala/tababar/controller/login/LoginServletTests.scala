package tababar.controller.login

import org.scalatra.test.scalatest._
import tababar.domain.model.User
import tababar.domain.service.UserService

class LoginServletTests extends ScalatraFunSuite {

  //addServlet(classOf[LoginController], "/*")
  val mockUserService = new UserService {
    override def add(email: String, password: String, name: String): Either[String, User] = ???

    override def find(id: String): Option[User] = ???

    override def authenticate(email: String, password: String): Either[String, User] = {
      password match {
        case "password" => Right(User("test@test","test","password"))
        case _          => Left("認証エラー")
      }
    }
  }
  addServlet(new LoginController(mockUserService),"/*")

  test("GET / on LoginServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

  test("POST /login success case"){
    post("/login",("email" -> "test@test"),("password" -> "password")){
      status should equal (302)
    }
  }

  test("POST /login form validation error"){
    post("/login",("password" -> "password")){
      status should equal (400)
    }
  }
  test("POST /login authentication error"){
    post("/login",("email" -> "test@test"),("password" -> "WrongPassword")){
      status should equal (401)
    }
  }


}
