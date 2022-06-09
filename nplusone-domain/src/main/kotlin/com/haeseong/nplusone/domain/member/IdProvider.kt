package com.haeseong.nplusone.domain.member

import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class IdProvider(
    @Enumerated(EnumType.STRING)
    val idProviderType: IdProviderType,
    val idProviderUserId: String,
)