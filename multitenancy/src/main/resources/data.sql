-- Tenant A 사용자
INSERT INTO users (name, email, tenant_id) VALUES ('Alice', 'alice@tenantA.com', 'tenantA');
INSERT INTO users (name, email, tenant_id) VALUES ('Bob', 'bob@tenantA.com', 'tenantA');
INSERT INTO users (name, email, tenant_id) VALUES ('Charlie', 'charlie@tenantA.com', 'tenantA');

-- Tenant B 사용자
INSERT INTO users (name, email, tenant_id) VALUES ('David', 'david@tenantB.com', 'tenantB');
INSERT INTO users (name, email, tenant_id) VALUES ('Eve', 'eve@tenantB.com', 'tenantB');

-- Tenant A 그룹
INSERT INTO `groups` (name, tenant_id) VALUES ('Engineering', 'tenantA');
INSERT INTO `groups` (name, tenant_id) VALUES ('Design', 'tenantA');

-- Tenant B 그룹
INSERT INTO `groups` (name, tenant_id) VALUES ('Marketing', 'tenantB');

-- Tenant A 스페이스 (Engineering 그룹 하위)
INSERT INTO spaces (name, group_id, tenant_id) VALUES ('Backend', 1, 'tenantA');
INSERT INTO spaces (name, group_id, tenant_id) VALUES ('Frontend', 1, 'tenantA');

-- Tenant A 스페이스 (Design 그룹 하위)
INSERT INTO spaces (name, group_id, tenant_id) VALUES ('UI/UX', 2, 'tenantA');

-- Tenant B 스페이스
INSERT INTO spaces (name, group_id, tenant_id) VALUES ('Campaign', 3, 'tenantB');

-- Tenant A 그룹-사용자 매핑
INSERT INTO group_users (group_id, user_id, tenant_id) VALUES (1, 1, 'tenantA');
INSERT INTO group_users (group_id, user_id, tenant_id) VALUES (1, 2, 'tenantA');
INSERT INTO group_users (group_id, user_id, tenant_id) VALUES (2, 3, 'tenantA');

-- Tenant B 그룹-사용자 매핑
INSERT INTO group_users (group_id, user_id, tenant_id) VALUES (3, 4, 'tenantB');
INSERT INTO group_users (group_id, user_id, tenant_id) VALUES (3, 5, 'tenantB');

-- Tenant A 스페이스-사용자 매핑
INSERT INTO space_users (space_id, user_id, tenant_id) VALUES (1, 1, 'tenantA');
INSERT INTO space_users (space_id, user_id, tenant_id) VALUES (2, 2, 'tenantA');
INSERT INTO space_users (space_id, user_id, tenant_id) VALUES (3, 3, 'tenantA');

-- Tenant B 스페이스-사용자 매핑
INSERT INTO space_users (space_id, user_id, tenant_id) VALUES (4, 4, 'tenantB');

-- Tenant A 개인 채팅방
INSERT INTO personal_rooms (user_id_1, user_id_2, tenant_id) VALUES (1, 2, 'tenantA');
INSERT INTO personal_rooms (user_id_1, user_id_2, tenant_id) VALUES (1, 3, 'tenantA');

-- Tenant B 개인 채팅방
INSERT INTO personal_rooms (user_id_1, user_id_2, tenant_id) VALUES (4, 5, 'tenantB');
