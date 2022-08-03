insert into tb_partner(id, name, parent_partner_id)
    values
        ('root-p-0', 'p1-name', null),
        ('root-p-1', 'p1-name', null),
        ('p2', 'p2-name', 'root-p-1'),
        ('p3', 'p3-name', 'root-p-1'),
        ('p4', 'p4-name', 'root-p-1'),
        ('p5', 'p5-name', 'root-p-1'),
        ('p6', 'p6-name', 'root-p-1'),
        ('p7', 'p7-name', 'root-p-0')
;