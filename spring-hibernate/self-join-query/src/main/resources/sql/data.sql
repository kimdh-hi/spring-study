insert into tb_partner(id, name, parent_partner_id)
    values
        ('p1', 'p1-name', null),
        ('p2', 'p2-name', 'p1'),
        ('p3', 'p3-name', 'p1'),
        ('p4', 'p4-name', 'p1'),
        ('p5', 'p5-name', 'p2'),
        ('p6', 'p6-name', 'p2')
;