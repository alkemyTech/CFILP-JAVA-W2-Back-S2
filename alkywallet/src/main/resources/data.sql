INSERT INTO usuarios (nombre, apellido, email,contrasenia ,estado) VALUES
('Juan', 'Pérez', 'juan.perez@example.com', '$2a$16$RdESrVgQzi8kaRxER4cto.84Bj88C.7gP9Mp66QblmE5rvIBDbi..',true),
('Ana', 'López', 'ana.lopez@example.com','$2a$16$leuv5tw8Snm522Awl1cTf.lNz0d0tA4F3OwG64ktqSXPFkL5Femum', true);

INSERT INTO roles (id_rol,rol_nombre) VALUES (1,'ADMIN');
INSERT INTO roles (id_rol,rol_nombre) VALUES (2,'USER');
-- INSERT INTO roles (id_rol,rol_nombre) VALUES (3,'INVITED');

INSERT INTO tbl_usuario_roles (id_usuario,id_rol) VALUES (1,1);
INSERT INTO tbl_usuario_roles (id_usuario,id_rol) VALUES (1,2);
INSERT INTO tbl_usuario_roles (id_usuario,id_rol) VALUES (2,2);


INSERT INTO cuentas (cbu, saldo, fecha_apertura, estado, moneda) VALUES
('1059127711100063105336',1500000.00, '2023-01-15', true,'ARS'),
('6321787211100052764841',2450000.75, '2023-03-10', true,'USD'),
('0813032311100080929014',8000000.50, '2022-12-01', false,'ARS'),
('7271961711100068475704',12990000.99, '2023-06-25', true,'USD'),
('3788914011100006218660',5000000.00, '2023-02-20', true,'ARS');

INSERT INTO tbl_usuario_cuentas (id_cuenta,id_usuario) VALUES (1,1);
INSERT INTO tbl_usuario_cuentas (id_cuenta,id_usuario) VALUES (2,1);
INSERT INTO tbl_usuario_cuentas (id_cuenta,id_usuario) VALUES (3,2);
INSERT INTO tbl_usuario_cuentas (id_cuenta,id_usuario) VALUES (4,2);
INSERT INTO tbl_usuario_cuentas (id_cuenta,id_usuario) VALUES (5,1);


INSERT INTO tarjetas (nombre_titular,tipo, fecha_vencimiento, estado, tope_gasto, numero_tarjeta, banco, cvv,marca) VALUES
('roberto carlos','CREDITO', '2026-07-01', true, 10000.00, '4111111111111111', 'Banco Uno', '123','MASTERCARD'),
('marcelo gallardo','ALKYWALLET', '2025-12-01', true, 3000.00, '4222222222222', 'Banco Dos', '456','ALKYWALLET'),
('juan manso','CREDITO', '2024-10-01', false, 2000.00, '4333333333333', 'Banco Tres', '789','MASTERCARD'),
('obi juan','DEBITO', '2026-01-01', true, 1500.00, '4444444444444', 'Banco Uno', '321','VISA'),
('guido','CREDITO', '2027-05-01', true, 5000.00, '4555555555555', 'Banco Cuatro', '654','MASTERCARD');

INSERT INTO tbl_cuenta_tarjeta(id_cuenta,id_tarjeta) VALUES (1,1);
INSERT INTO tbl_cuenta_tarjeta(id_cuenta,id_tarjeta) VALUES (2,2);
INSERT INTO tbl_cuenta_tarjeta(id_cuenta,id_tarjeta) VALUES (1,3);
INSERT INTO tbl_cuenta_tarjeta(id_cuenta,id_tarjeta) VALUES (2,4);
INSERT INTO tbl_cuenta_tarjeta(id_cuenta,id_tarjeta) VALUES (1,5);

-- DEPOSITOS
INSERT INTO deposito (monto,fecha,medio_de_pago,estado,canal, origen_de_fondos,id_externo)
VALUES ('1000000','2025-06-09T12:03:56','efectivo',true,'rapipago','rapipago',UUID());

INSERT INTO deposito (monto,fecha,medio_de_pago,estado,canal, origen_de_fondos,id_externo)
VALUES ('156897','2025-06-09T12:03:56','efectivo',true,'pagofacil','pagofacil',UUID());

--TABLA INTERMEDIA CUENTA-DEPOSITO
INSERT INTO tbl_cuenta_deposito (id_cuenta, id_transaccion) VALUES (1,1);
INSERT INTO tbl_cuenta_deposito (id_cuenta, id_transaccion) VALUES (1,2);

--TRANSFERENCIAS
INSERT INTO transferencia (monto,fecha,medio_de_pago,estado,tipo_transferencia,motivo,nombre_destinatario,banco_destino,cuenta_destinatario,cuenta_origen)
VALUES ('250000','2025-06-25T10:33:56','transferencia',true,'debito','pago de alquiler','alberto paniagua','BancoDos','trabajar esto','trabajar esto');

INSERT INTO transferencia (monto,fecha,medio_de_pago,estado,tipo_transferencia,motivo,nombre_destinatario,banco_destino,cuenta_destinatario,cuenta_origen)
VALUES ('50000','2025-06-10T10:30:56','transferencia',true,'debito','varios','Totec.S.A','BancoTres','trabajar esto','trabajar esto');

INSERT INTO transferencia (monto,fecha,medio_de_pago,estado,tipo_transferencia,motivo,nombre_destinatario,banco_destino,cuenta_destinatario,cuenta_origen)
VALUES ('25000','2025-06-15T10:30:56','transferencia',true,'credito','alimentos y bebidas','LaAnonima.S.A','Banco8','2135421682165','789456123');

INSERT INTO transferencia (monto,fecha,medio_de_pago,estado,tipo_transferencia,motivo,nombre_destinatario,banco_destino,cuenta_destinatario,cuenta_origen)
VALUES ('250000','2025-06-21T12:30:56','transferencia',true,'debito','pago de alquiler','ReMax.S.A','Banco9','trabajar esto','trabajar esto');

-- TABLA INTERMEDIA CUENTA-TRANSFERENCIA
INSERT INTO tbl_cuenta_transferencia (id_cuenta, id_transaccion) VALUES (1,1);
INSERT INTO tbl_cuenta_transferencia (id_cuenta, id_transaccion) VALUES (1,2);

--TABLA INTERMEDIA TARJETA-TRANSFERENCIA
INSERT INTO tbl_tarjeta_transferencia(id_tarjeta,id_transaccion) VALUES (1,3);
INSERT INTO tbl_tarjeta_transferencia(id_tarjeta,id_transaccion) VALUES (2,4);


-- ALMACENAMIENTO DE SALDO
INSERT INTO almacenamiento_saldo (monto,fecha,medio_de_pago,estado,codigo_referencia,comision_aplicada)
VALUES ('558960','2025-09-27T12:03:56','pago a cuenta',true,UUID(),1.20);

INSERT INTO almacenamiento_saldo (monto,fecha,medio_de_pago,estado,codigo_referencia,comision_aplicada)
VALUES ('25000','2025-09-27T12:03:56','pago a cuenta',true,UUID(),1.15);

-- TABLA INTERMEDIA CUENTA-ALMACENAMIENTO_SALDO
INSERT INTO tbl_cuenta_almacenamiento (id_cuenta, id_transaccion) VALUES (1,1);
INSERT INTO tbl_cuenta_almacenamiento (id_cuenta, id_transaccion) VALUES (1,2);