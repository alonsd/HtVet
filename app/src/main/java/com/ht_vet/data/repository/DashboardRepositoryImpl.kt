package com.ht_vet.data.repository

import com.ht_vet.data.source.remote.source.hero.RemoteDataSource
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : DashboardRepository {

}

