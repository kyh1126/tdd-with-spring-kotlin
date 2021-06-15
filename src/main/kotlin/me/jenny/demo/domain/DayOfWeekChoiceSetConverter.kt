package me.jenny.demo.domain

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class DayOfWeekChoiceSetConverter : AttributeConverter<Set<DayOfWeekChoice>, String> {
    private val splitter = ","

    override fun convertToDatabaseColumn(attribute: Set<DayOfWeekChoice>) = attribute.joinToString(splitter)

    override fun convertToEntityAttribute(dbData: String) =
        dbData.split(splitter).map { DayOfWeekChoice.valueOf(it) }.toSet()
}
