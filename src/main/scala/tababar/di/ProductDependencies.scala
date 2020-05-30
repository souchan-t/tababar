package tababar.di

import com.google.inject.{AbstractModule, TypeLiteral}

/**
 * DI(Guice)による依存性の設定モジュール
 */
object ProductDependencies extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[tababar.util.GlobalConfig])
      .to(classOf[tababar.util.ResourceGlobalConfig])

    bind(new TypeLiteral[tababar.domain.repository.UserRepository[scalikejdbc.DBSession]]() {})
      .to(classOf[tababar.infra.domain.repository.DBUserRepository])

    bind(classOf[tababar.domain.service.UserService])
      .to(classOf[tababar.infra.domain.service.UserServiceDb])

    bind(new TypeLiteral[tababar.domain.repository.TrainingRepository[scalikejdbc.DBSession]]() {})
      .to(classOf[tababar.infra.domain.repository.DBTrainingRepository])

    bind(classOf[tababar.domain.service.TrainingService])
      .to(classOf[tababar.infra.domain.service.TrainingServiceDb])


  }
}

