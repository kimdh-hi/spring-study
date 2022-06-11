insert into tb_partner
    (id, name, parent_partner_id)
values
   ('p-1', 'rsup', null),
   ('p-2', 'web1', 'p-1'),
   ('p-3', 'web2', 'p-1'),
   ('p-4', 'rv-back', 'p-2'),
   ('p-5', 'rv-front', 'p-3')
;
