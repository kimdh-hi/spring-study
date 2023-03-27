insert into tb_collection_projection_parent(id, data1) values
('d1-1', 'data1'),
('d1-2', 'data2')
;

insert into tb_collection_projection_children(id, data2, collection_projection_parent_id) values
('d2-1', 'data1', 'd1-1'),
('d2-2', 'data1', 'd1-1'),
('d2-3', 'data1', 'd1-2'),
('d2-4', 'data2', 'd1-2')
;