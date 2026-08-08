package com.study.hierarchicalquery.tree.domain.repository

import com.study.hierarchicalquery.tree.domain.model.Tree
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TreeRepository : JpaRepository<Tree, String> {

  @Query("select t from Tree t where t.parent.id in :parentIds")
  fun findAllByParentIdIn(@Param("parentIds") parentIds: List<String>): List<Tree>

  @Query(
    value = """
      with recursive sub(id, name, path, parent_id) as (
        select n.id, n.name, n.path, n.parent_id from tree n where n.id = :id
        union all
        select c.id, c.name, c.path, c.parent_id from tree c join sub s on c.parent_id = s.id
      )
      select * from sub
    """,
    nativeQuery = true,
  )
  fun findSubtreeByCte(@Param("id") id: String): List<Tree>
}
