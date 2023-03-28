insert into tb_collection_projection_parent(id, data1) values
('d1-1', 'data1'),
('d1-2', 'data2'),
('d1-3', 'data3'),
('d1-4', 'data4')
;

insert into tb_collection_projection_child(id, data2, collection_projection_parent_id) values
('d2-1', 'data1', 'd1-1'),
('d2-2', 'data2', 'd1-1'),
('d2-3', 'data3', 'd1-2'),
('d2-4', 'data4', 'd1-2'),
('d2-5', 'data5', 'd1-3'),
('d2-6', 'data6', 'd1-3'),
('d2-7', 'data6', 'd1-4'),
('d2-8', 'data6', 'd1-4')
;