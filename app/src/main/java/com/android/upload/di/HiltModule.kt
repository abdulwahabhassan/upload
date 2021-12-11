package com.android.upload.di

import com.android.upload.data.api.DarotApi
import com.android.upload.data.api.ServiceModule
import com.android.upload.data.datasource.UploadDatasource
import com.android.upload.data.datasource.UploadDatasourceImpl
import com.android.upload.data.mapper.ResponseMapper
import com.android.upload.data.repo.RepositoryImpl
import com.android.upload.domain.repo.Repository
import com.android.upload.domain.usecase.UploadImageUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DarotApiModule {

    private const val DAROT_API_BASE_URL = "https://darot-image-upload-service.herokuapp.com/api/v1/"

    @Provides
    fun providesDarotApi () : DarotApi {
        return ServiceModule().createDarotApi(DAROT_API_BASE_URL)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    fun provideResponseMapper() : ResponseMapper = ResponseMapper()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UploadDatasourceModule {

    @Binds
    abstract fun bindUploadDatasource(
        remoteDatasourceImpl: UploadDatasourceImpl
    ): UploadDatasource
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {

    @Singleton
    @Provides
    fun provideDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepository(
        source: UploadDatasource
    ): Repository {
        return RepositoryImpl(
            source
        )
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @ViewModelScoped
    @Provides
    fun provideUploadImageUseCase(
        repository: Repository
    ) : UploadImageUseCase {
        return UploadImageUseCase(repository)
    }


}