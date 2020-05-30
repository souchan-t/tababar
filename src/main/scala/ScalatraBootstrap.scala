import com.google.inject.Guice
import javax.servlet.ServletContext
import org.scalatra._
import tababar.controller.admin.AdminController
import tababar.controller.common._
import tababar.controller.login.{LoginController, LogoutController, SignUpController}
import tababar.controller.training.{ReservationController, TrainingController}
import tababar.controller.user.UserController
import tababar.db.Database
import tababar.di.ProductDependencies
import tababar.util.{GlobalConfig, IOExecutor}

class ScalatraBootstrap extends LifeCycle {
  val injector = Guice.createInjector(ProductDependencies)

  //データベースのコネクションプール初期化
  val database: Database = injector.getInstance(classOf[Database])

  //非同期実行時のスレッドプール初期化
  val IOExecutor = injector.getInstance(classOf[IOExecutor])

  override def init(context: ServletContext) {



    //context.mount(injector.getInstance(classOf[LoggingFilter]), "/*")
    context.mount(injector.getInstance(classOf[LoginController]), "/*")
    context.mount(injector.getInstance(classOf[LogoutController]), "/logout")
    context.mount(injector.getInstance(classOf[SignUpController]), "/signup")
    context.mount(injector.getInstance(classOf[UserController]), "/users")
    context.mount(injector.getInstance(classOf[AdminController]), "/admin")
    context.mount(injector.getInstance(classOf[TrainingController]), "/training")
    context.mount(injector.getInstance(classOf[ReservationController]),"/reservation")


  }

  override def destroy(context: ServletContext): Unit = {
    super.destroy(context)
    //コネクションプールの接続とデータソースを閉じる
    this.database.close()

    //非同期実行のスレッドプール終了
    this.IOExecutor.shutdown()

  }
}
