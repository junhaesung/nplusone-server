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
                .toInstant(ZONE_OFFSET_SEOUL))
        }
    }

    override fun convertToEntityAttribute(dbData: Date?): YearMonth? {
        return dbData?.toInstant()?.run { YearMonth.from(this.atZone(ZONE_OFFSET_SEOUL)) }
    }

    companion object {
        val ZONE_OFFSET_SEOUL: ZoneOffset = ZoneOffset.of("+09:00")
    }
}