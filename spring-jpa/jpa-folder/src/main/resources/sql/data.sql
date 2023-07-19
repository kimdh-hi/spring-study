insert into tb_file_folder(id, name, parent_folder_id) values
    ('ff1', 'root1', null),
    ('ff2', 'root2', null),
    ('ff3', 'root1-child1', 'ff1'),
    ('ff4', 'root1-child2', 'ff1'),
    ('ff5', 'root2-child1', 'ff2')
;