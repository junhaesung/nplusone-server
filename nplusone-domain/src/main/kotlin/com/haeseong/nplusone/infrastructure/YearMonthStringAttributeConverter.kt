package com.haeseong.nplusone.infrastructure

import java.time.YearMonth
import java.time.ZoneOffset
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class YearMonthStringAttributeConverter : AttributeConverter<YearMonth, Date> {
    override fun convertToDatabaseColumn(attribute: YearMonth?): Date? {
        return attribute?.run {
            Date.from(this.atDay(1).atStartOfDay()
                .toInstant(ZoneOffset.of("+09:00")))
        }
    }

    override fun convertToEntityAttribute(dbData: Date?): YearMonth? {
        return dbData?.toInstant()?.run { YearMonth.from(this) }
    }
}