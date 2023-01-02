# Spring graphql (webflux)

### Fragment
Query 의 일부분을 재사용하기 위해 사용한다.
```graphql
{
  customers {
    ... CustomerFragment
  }
  customersByAgeRange(filter: {
    minAge: 6, maxAge: 50
  }) {
    ... CustomerFragment
  }
}

fragment CustomerFragment on Customer {
 id, name, age
}
```