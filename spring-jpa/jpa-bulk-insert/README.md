## jpa bulk insert

mariadb
- `rewriteBatchedStatements` 옵션 제거됨
- https://mariadb.com/kb/en/about-mariadb-connector-j/#removed-option

bulk insert log 확인
```yml
logging:
  level:
    "[org.mariadb.jdbc]": trace
```