# Spring graphql (webflux)

### Fragment
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