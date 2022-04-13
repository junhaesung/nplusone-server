package com.haeseong.nplusone.infrastructure

import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class YearMonthStringAttributeConverter : AttributeConverter<YearMonth, String> {
    override fun convertToDatabaseColumn(attribute: YearMonth?): String? {
        return attribute?.format(DateTimeFormatter.ofPattern("yyyy-MM"))
    }

    override fun convertToEntityAttribute(dbData: String?): YearMonth? {
        return dbData?.run { YearMonth.parse(this) }
    }
}