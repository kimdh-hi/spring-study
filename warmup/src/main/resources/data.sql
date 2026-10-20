INSERT INTO product (name, category, price)
SELECT 'product-' || x,
       CASE MOD(x, 3) WHEN 0 THEN 'BOOK' WHEN 1 THEN 'FOOD' ELSE 'TOY' END,
       MOD(x * 7919, 100000)
FROM SYSTEM_RANGE(1, 1000);
