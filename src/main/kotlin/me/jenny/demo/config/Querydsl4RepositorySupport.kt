package me.jenny.demo.config

import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport
import org.springframework.data.jpa.repository.support.Querydsl
import org.springframework.data.querydsl.SimpleEntityPathResolver
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.util.Assert
import java.util.function.Function
import javax.annotation.PostConstruct
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Querydsl 4.x 버전에 맞춘 Querydsl 지원 라이브러리
 * @see     org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
 */
open class Querydsl4RepositorySupport(private val domainClass: Class<*>) {
    private lateinit var querydsl: Querydsl
    private lateinit var entityManager: EntityManager
    private lateinit var queryFactory: JPAQueryFactory

    @PersistenceContext
    protected fun setEntityManager(entityManager: EntityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!")

        val entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager)
        val path: EntityPath<*> = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.javaType)

        this.entityManager = entityManager
        querydsl = Querydsl(entityManager, PathBuilder(path.type, path.metadata))
        queryFactory = JPAQueryFactory(entityManager)
    }

    @PostConstruct
    fun validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!")
        Assert.notNull(querydsl, "Querydsl must not be null!")
        Assert.notNull(queryFactory, "QueryFactory must not be null!")
    }

    protected fun getEntityManager() = entityManager

    protected fun <T> select(expr: Expression<T>): JPAQuery<T> = queryFactory.select(expr)

    protected fun <T> selectFrom(from: EntityPath<T>): JPAQuery<T> = queryFactory.selectFrom(from)

    protected fun <T> applyPagination(
        pageable: Pageable,
        contentQuery: Function<JPAQueryFactory, JPAQuery<*>>
    ): Page<T> {
        val jpaQuery: JPAQuery<*> = contentQuery.apply(queryFactory)
        val content: List<T> = querydsl.applyPagination(pageable, jpaQuery).fetch() as List<T>
        return PageableExecutionUtils.getPage(content, pageable) { jpaQuery.fetchCount() }
    }

    protected fun <T> applyPagination(
        pageable: Pageable,
        contentQuery: Function<JPAQueryFactory, JPAQuery<*>>,
        countQuery: Function<JPAQueryFactory, JPAQuery<*>>
    ): Page<T> {
        val jpaContentQuery: JPAQuery<*> = contentQuery.apply(queryFactory)
        val content: List<T> = querydsl.applyPagination(pageable, jpaContentQuery).fetch() as List<T>
        val countResult: JPAQuery<*> = countQuery.apply(queryFactory)
        return PageableExecutionUtils.getPage(content, pageable) { countResult.fetchCount() }
    }
}
