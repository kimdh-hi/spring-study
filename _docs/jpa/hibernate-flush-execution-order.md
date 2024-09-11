## hibernate flush execution order

order
1. Inserts, in the order they were performed
2. Updates
3. Deletion of collection elements
4. Insertion of collection elements
5. Deletes, in the order they were performed

코드 순서상 delete 호출 후 insert 를 호출했더라도, 실제 쿼리는 insert -> delete 순서로 발생될 수 있음.

reference
- https://docs.jboss.org/hibernate/orm/6.1/javadocs/org/hibernate/event/internal/AbstractFlushingEventListener.html#performExecutions(org.hibernate.event.spi.EventSource)