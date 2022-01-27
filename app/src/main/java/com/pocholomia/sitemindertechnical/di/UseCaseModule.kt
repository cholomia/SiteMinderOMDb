package com.pocholomia.sitemindertechnical.di

import com.pocholomia.sitemindertechnical.data.MovieUseCaseImpl
import com.pocholomia.sitemindertechnical.domain.MovieUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun movieUseCase(impl: MovieUseCaseImpl): MovieUseCase

}